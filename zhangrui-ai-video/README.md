# PPT-to-Video Converter

A powerful, **ffmpeg-based** automation tool designed to synthesize high-quality videos from PowerPoint images, slide scripts (audio), and existing video clips.

## 🚀 Overview

This project provides a streamlined solution for turning static presentations into dynamic video content. By combining slide images with corresponding audio narrations and transition videos, it automates the tedious manual editing process.

### Key Features

* **Image-Audio Sync:** Automatically matches slide images with their respective audio scripts.
* **Video Embedding:** Seamlessly integrates video clips between or during slides.
* **FFmpeg Powered:** Uses the industry-standard multimedia framework for fast and lossless processing.
* **Flexible Export:** Supports various formats (MP4, MKV, AVI) and customizable resolutions.

---


## ⚙️ How It Works

The tool processes the assets through a multi-stage **FFmpeg pipeline**:

1. **Scaling:** All images and videos are normalized to a consistent resolution (e.g., 1080p).
2. **Audio Padding:** Adjusts image duration to match the length of the audio file.
3. **Concatenation:** Merges the processed segments using the `concat` demuxer or filter.
4. **Encoding:** Renders the final stream using `libx264` for maximum compatibility.

---

## 📝 License

Distributed under the Apache License 2.0. See `LICENSE` for more information.