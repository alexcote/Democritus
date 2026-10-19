# Democritus: Molecular Dynamics Simulation (Modern Edition)

A modern, web-native restoration of the educational molecular dynamics suite originally created at Daresbury Laboratory in 2000.

---

## About This Modern Edition

Originally created at Daresbury Laboratory in 2000, **Democritus** was designed to give students an intuitive, visual playground for exploring molecular dynamics principles.

This updated edition faithfully revives the original simulation engine for modern web browsers. While the legacy Java applet has been rebuilt in clean, native JavaScript and HTML5 Canvas, deliberate care has been taken to preserve the original visual identity, physics integrators, and classic interface design. Enhanced with fluid scaling for modern high-resolution displays and mobile devices, the engine delivers the same classic interactive learning experience—without requiring outdated browser plugins.

---

## Key Features

- **Zero-Dependency Native Stack**: Runs directly in any modern browser using pure HTML5, CSS3, and ES6 JavaScript (no Java runtime or browser plugins required).
- **Faithful Physics Engine**:
  - Velocity Verlet numerical integrator with shifted-force Lennard-Jones potential.
  - Periodic boundary conditions (minimum image convention).
  - Velocity rescaling thermostat with periodic momentum corrections.
  - Built-in real-time correlation functions:
    - **RDF** (Radial Distribution Function)
    - **MSD** (Mean Squared Displacement)
    - **VAC** (Velocity Autocorrelation Function)
- **Authentic Period Aesthetic**: Preserves the original 1990s/2000s desktop look, textured bevels, 3D embossed plaques, and tactical interface controls.
- **High-DPI & Mobile Responsive**:
  - Dynamically scales to crisp 1440p and 4K displays while maintaining proportional canvas dimensions.
  - Fully responsive split-screen and stacked views for desktop, tablet, and mobile browsers.
  - High-DPI canvas scaling for sharp particle rendering.

---

## Project Structure

```text
.
├── index.html           # Root entry point (GitHub Pages landing)
├── Basic/               # Main index and documentation pages
├── Theory/              # Background reading, theory, and pedagogical notes
├── Experiments/         # Interactive experiment suite (exper1a - exper8c)
│   ├── MD.html          # Core simulation engine and controls
│   ├── expintr.html     # Experiments menu and navigation
│   ├── exps.html        # Main split-screen shell
│   └── Graph.html       # Dynamic plotting window for RDF/MSD/VAC
├── Icons/               # Interface icons and legacy navigation graphics
├── Textures/            # Background textures and period assets
├── responsive.css       # Unified responsive layout and typography stylesheet
└── README.md            # Project documentation
```

---


## Attribution & Heritage

* **Original Democritus Project (2000):** Developed at Daresbury Laboratory (CCLRC / STFC) by Alex Côté, Bill Smith and Phil Lindan.
* **Modern HTML5 & PWA Edition (2026):** Re-engineered and maintained by Alex Côté.

### License

* **Code:** The modern HTML5, CSS, and native JavaScript simulation engine is licensed under the [MIT License](LICENSE).
* **Educational & Theoretical Content:** The original explanatory texts and pedagogical materials are preserved for educational and non-commercial classroom use under [CC BY-NC 4.0](https://creativecommons.org/licenses/by-nc/4.0/).
* Original project materials &copy; 2000 Daresbury Laboratory / CCLRC.