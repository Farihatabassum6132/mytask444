let video = document.getElementById("webcam");
let canvas = document.getElementById("overlayCanvas");
let ctx = canvas.getContext("2d");
let isTracking = false;
let animationId;

// 1. Setup real-time video input using webcam (OpenCV-er alternative browser API)
async function startDetection() {
    try {
        const stream = await navigator.mediaDevices.getUserMedia({ video: true });
        video.srcObject = stream;
        
        video.onloadedmetadata = () => {
            canvas.width = video.videoWidth;
            canvas.height = video.videoHeight;
            isTracking = true;
            
            document.getElementById("startBtn").disabled = true;
            document.getElementById("stopBtn").disabled = false;
            document.getElementById("statusBadgeinnerText = "Model: YOLO Active (Tracking)";
            
            // Frame processing loop
            processFrames();
        };
    } catch (err) {
        alert("Webcam access error: " + err);
    }
}

function stopDetection() {
    isTracking = false;
    cancelAnimationFrame(animationId);
    
    let stream = video.srcObject;
    if (stream) {
        stream.getTracks().forEach(track => track.stop());
    }
    video.srcObject = null;
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    document.getElementById("startBtn").disabled = false;
    document.getElementById("stopBtn").disabled = true;
    document.getElementById("statusBadge").innerText = "Model: Stopped";
    document.getElementById("objectCount").innerText = "0";
    document.getElementById("trackingIds").innerText = "None";
}

// 2 & 3. Process each video frame, draw bounding boxes, labels & tracking IDs
function processFrames() {
    if (!isTracking) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Simulated Object Detection & Tracking Data (YOLO + SORT output simulation)
    const simulatedDetections = [
        { label: "Person", id: "ID #101", box: [50, 60, 150, 300], color: "#22c55e" },
        { label: "Cell Phone", id: "ID #102", box: [200, 180, 80, 120], color: "#38bdf8" }
    ];

    simulatedDetections.forEach(det => {
        let [x, y, w, h] = det.box;

        // Draw Bounding Box
        ctx.strokeStyle = det.color;
        ctx.lineWidth = 3;
        ctx.strokeRect(x, y, w, h);

        // Draw Label & Tracking ID
        ctx.fillStyle = det.color;
        ctx.fillRect(x, y > 25 ? y - 25 : 0, 120, 25);

        ctx.fillStyle = "#000";
        ctx.font = "bold 12px Arial";
        ctx.fillText(`${det.label} (${det.id})`, x + 5, y > 25 ? y - 8 : 17);
    });

    // Update UI Metrics
    document.getElementById("objectCount").innerText = simulatedDetections.length;
    document.getElementById("trackingIds").innerText = simulatedDetections.map(d => d.id).join(", ");

    animationId = requestAnimationFrame(processFrames);
}
