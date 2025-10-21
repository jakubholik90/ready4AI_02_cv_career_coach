// DOM Elements
const uploadArea = document.getElementById('uploadArea');
const fileInput = document.getElementById('fileInput');
const cvUploadForm = document.getElementById('cvUploadForm');
const fileInfo = document.getElementById('fileInfo');
const fileName = document.getElementById('fileName');
const fileSize = document.getElementById('fileSize');
const removeFileBtn = document.getElementById('removeFile');
const analyzeBtn = document.getElementById('analyzeBtn');
const loader = document.getElementById('loader');
const uploadSection = document.getElementById('uploadSection');
const resultsSection = document.getElementById('resultsSection');
const cvDataResult = document.getElementById('cvDataResult');
const findMatchingBtn = document.getElementById('findMatchingBtn');
const findAlternativeBtn = document.getElementById('findAlternativeBtn');
const jobsResult = document.getElementById('jobsResult');
const jobsTitle = document.getElementById('jobsTitle');
const jobsList = document.getElementById('jobsList');
const errorSection = document.getElementById('errorSection');
const errorMessage = document.getElementById('errorMessage');

let selectedFile = null;

// API Base URL (change for production)
const API_BASE = window.location.origin;

// ============================================
// Drag & Drop Handlers
// ============================================

uploadArea.addEventListener('dragover', (e) => {
    e.preventDefault();
    uploadArea.classList.add('dragover');
});

uploadArea.addEventListener('dragleave', () => {
    uploadArea.classList.remove('dragover');
});

uploadArea.addEventListener('drop', (e) => {
    e.preventDefault();
    uploadArea.classList.remove('dragover');

    const files = e.dataTransfer.files;
    if (files.length > 0) {
        const file = files[0];
        if (validateFile(file)) {
            handleFileSelect(file);
        }
    }
});

// Click to upload
uploadArea.addEventListener('click', () => {
    fileInput.click();
});

// File input change
fileInput.addEventListener('change', (e) => {
    if (e.target.files.length > 0) {
        const file = e.target.files[0];
        if (validateFile(file)) {
            handleFileSelect(file);
        }
    }
});

// ============================================
// File Handling
// ============================================

function validateFile(file) {
    // Check file type
    if (file.type !== 'application/pdf' && !file.name.toLowerCase().endsWith('.pdf')) {
        showError('Proszę wybrać plik PDF. Wybrany plik: ' + file.type);
        return false;
    }

    // Check file size (5MB = 5 * 1024 * 1024 bytes)
    const maxSize = 5 * 1024 * 1024;
    if (file.size > maxSize) {
        showError(`Plik jest za duży (${formatFileSize(file.size)}). Maksymalny rozmiar to 5MB.`);
        return false;
    }

    return true;
}

function handleFileSelect(file) {
    selectedFile = file;
    fileName.textContent = file.name;
    fileSize.textContent = formatFileSize(file.size);
    fileInfo.classList.remove('hidden');
    analyzeBtn.disabled = false;
    hideError();
}

function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(1) + ' MB';
}

// Remove file
removeFileBtn.addEventListener('click', (e) => {
    e.stopPropagation();
    resetFileInput();
});

function resetFileInput() {
    selectedFile = null;
    fileInput.value = '';
    fileInfo.classList.add('hidden');
    analyzeBtn.disabled = true;
}

// ============================================
// Form Submit - Upload and Analyze CV
// ============================================

cvUploadForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    if (!selectedFile) {
        showError('Proszę wybrać plik PDF');
        return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
        // Show loader
        loader.classList.remove('hidden');
        analyzeBtn.disabled = true;
        hideError();
        jobsResult.classList.add('hidden');
        resultsSection.classList.add('hidden');

        // Upload PDF
        const response = await fetch(`${API_BASE}/api/cv/upload`, {
            method: 'POST',
            body: formData
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Błąd podczas analizy CV');
        }

        const cvData = await response.json();

        // Display results
        displayCvData(cvData);
        resultsSection.classList.remove('hidden');

        // Smooth scroll to results
        resultsSection.scrollIntoView({ behavior: 'smooth', block: 'start' });

    } catch (error) {
        console.error('Error:', error);
        showError(error.message || 'Wystąpił nieoczekiwany błąd podczas analizy CV');
    } finally {
        loader.classList.add('hidden');
        analyzeBtn.disabled = false;
    }
});

// ============================================
// Display CV Data
// ============================================

function displayCvData(data) {
    cvDataResult.innerHTML = `
        <h3>✨ Wyniki analizy AI</h3>
        <p><strong>📍 Lokalizacja:</strong> ${escapeHtml(data.location)}</p>
        <p><strong>💼 Branża:</strong> ${escapeHtml(data.jobBranch)}</p>
        <p><strong>🎓 Wykształcenie:</strong> ${escapeHtml(data.education)}</p>
        <p><strong>⏱️ Doświadczenie:</strong> ${data.totalExperienceYears} ${pluralize(data.totalExperienceYears, 'rok', 'lata', 'lat')}
           (w tym ${data.branchExperienceYears} ${pluralize(data.branchExperienceYears, 'rok', 'lata', 'lat')} w branży)</p>
        <p><strong>🔧 Hard Skills:</strong> ${escapeHtml(data.hardSkills)}</p>
        <p><strong>💡 Soft Skills:</strong> ${escapeHtml(data.softSkills)}</p>
    `;
}

// ============================================
// Job Search
// ============================================

// Find matching jobs
findMatchingBtn.addEventListener('click', async () => {
    await fetchJobs('/api/cv/jobs/matching', '🎯 Dopasowane oferty pracy dla Ciebie');
});

// Find alternative jobs
findAlternativeBtn.addEventListener('click', async () => {
    await fetchJobs('/api/cv/jobs/alternative', '🔄 Alternatywne ścieżki kariery');
});

// Fetch jobs from API
async function fetchJobs(endpoint, title) {
    try {
        loader.classList.remove('hidden');
        jobsResult.classList.add('hidden');
        hideError();

        const response = await fetch(`${API_BASE}${endpoint}`);

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Nie znaleziono danych CV');
        }

        const jobs = await response.json();

        if (!jobs || jobs.length === 0) {
            showError('Nie znaleziono ofert pracy. Spróbuj ponownie później.');
            return;
        }

        displayJobs(jobs, title);

        // Smooth scroll to jobs
        jobsResult.scrollIntoView({ behavior: 'smooth', block: 'start' });

    } catch (error) {
        console.error('Error:', error);
        showError(error.message || 'Wystąpił błąd podczas wyszukiwania ofert pracy');
    } finally {
        loader.classList.add('hidden');
    }
}

// ============================================
// Display Jobs
// ============================================

function displayJobs(jobs, title) {
    jobsTitle.textContent = title;
    jobsList.innerHTML = '';

    jobs.forEach((job, index) => {
        const jobCard = document.createElement('div');
        jobCard.className = 'job-card';
        jobCard.innerHTML = `
            <h3>${index + 1}. ${escapeHtml(job.position)}</h3>
            <p class="company">🏢 ${escapeHtml(job.company)}</p>
            <div class="requirements">
                <strong>📋 Wymagania:</strong>
                <p>${escapeHtml(job.requirements)}</p>
            </div>
            <div class="match-reason">
                <strong>✨ Dlaczego pasuje:</strong>
                <p>${escapeHtml(job.matchReason)}</p>
            </div>
        `;

        // Add animation delay
        jobCard.style.animation = `fadeIn 0.5s ease ${index * 0.1}s both`;

        jobsList.appendChild(jobCard);
    });

    jobsResult.classList.remove('hidden');
}

// ============================================
// Error Handling
// ============================================

function showError(message) {
    errorMessage.textContent = message;
    errorSection.classList.remove('hidden');

    // Scroll to error
    errorSection.scrollIntoView({ behavior: 'smooth', block: 'center' });

    // Auto-hide after 8 seconds
    setTimeout(() => {
        hideError();
    }, 8000);
}

function hideError() {
    errorSection.classList.add('hidden');
}

// ============================================
// Utility Functions
// ============================================

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

function pluralize(number, singular, few, many) {
    if (number === 1) return singular;
    if (number % 10 >= 2 && number % 10 <= 4 && (number % 100 < 10 || number % 100 >= 20)) {
        return few;
    }
    return many;
}

// ============================================
// Initialize
// ============================================

console.log('CV Career Coach - Frontend initialized');
console.log('API Base URL:', API_BASE);