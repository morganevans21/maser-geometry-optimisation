"""
Exploratory data analysis and visualisation.

This module loads optimisation results from the project's ``results/``
directory and provides reusable functions for generating exploratory plots.

Expected project structure
--------------------------
project/
├── python/
│   └── data_analysis.py
├── results/
│   ├── processed/
│   │   ├── ga-logs.csv
│   │   ├── ga-results.csv
│   │   ├── permittivity-scaling-results.csv
│   │   └── spurious-mode-results.csv
│   ├── raw/
│   └── plots/
└── ...

The plotting functions return Matplotlib Figure objects and save the
corresponding figures to ``results/plots/``.
"""

from __future__ import annotations

from pathlib import Path

import matplotlib.pyplot as plt
import matplotlib as mpl
import pandas as pd


# ---------------------------------------------------------------------------
# Project paths
# ---------------------------------------------------------------------------

PYTHON_DIR = Path(__file__).resolve().parent

PROJECT_ROOT = PYTHON_DIR.parent

FONTS_DIR = PYTHON_DIR / "fonts"
LATIN_MODERN_FONT = FONTS_DIR / "lmroman12-regular.otf"

RESULTS_DIR = PROJECT_ROOT / "results" / "processed"
PLOTS_DIR = RESULTS_DIR / "plots"


# ---------------------------------------------------------------------------
# Plot configuration
# ---------------------------------------------------------------------------

FIGURE_SIZE = (10, 6)

PRIMARY_COLOUR = "#11316E"
SECONDARY_COLOUR = "orchid"
INDIVIDUAL_COLOUR = "blue"

X_LABEL_SIZE = 28
Y_LABEL_SIZE = 28
TICK_LABEL_SIZE = 24
OFFSET_TEXT_SIZE = 20

MOVING_AVERAGE_WINDOW = 25
MAX_GENERATION = 250

def configure_plot_style() -> None:
    """Configure the Matplotlib style used throughout the analysis.

    Latin Modern is loaded from the project's fonts directory so that
    figures are reproducible across different computers and operating
    systems.
    """

    if not LATIN_MODERN_FONT.exists():
        raise FileNotFoundError(
            f"Latin Modern font not found: {LATIN_MODERN_FONT}"
        )

    fm.fontManager.addfont(LATIN_MODERN_FONT)

    font_properties = fm.FontProperties(fname=LATIN_MODERN_FONT)
    font_name = font_properties.get_name()

    mpl.rcParams.update(
        {
            "font.family": font_name,
            "mathtext.fontset": "cm",
            "axes.labelsize": Y_LABEL_SIZE,
            "xtick.labelsize": TICK_LABEL_SIZE,
            "ytick.labelsize": TICK_LABEL_SIZE,
        }
    )

# ---------------------------------------------------------------------------
# Data loading
# ---------------------------------------------------------------------------

def load_data(filename: str) -> pd.DataFrame:
    """Load a CSV file from the project's results directory.

    Parameters
    ----------
    filename:
        Name of the CSV file located in ``results/``.

    Returns
    -------
    pandas.DataFrame
        The loaded dataset.

    Raises
    ------
    FileNotFoundError
        If the requested CSV file does not exist.
    """

    filepath = RESULTS_DIR / filename

    if not filepath.exists():
        raise FileNotFoundError(
            f"Could not find '{filename}' in {RESULTS_DIR}."
        )

    return pd.read_csv(filepath)


def load_all_data() -> dict[str, pd.DataFrame]:
    """Load all datasets required for the exploratory analysis.

    Returns
    -------
    dict[str, pandas.DataFrame]
        Dictionary containing the four analysis datasets.
    """

    return {
        "results": load_data("ga-results.csv"),
        "logs": load_data("ga-logs.csv"),
        "spurious": load_data("spurious-mode-results.csv"),
        "permittivity": load_data("permittivity-scaling-results.csv"),
    }


# ---------------------------------------------------------------------------
# Plot utilities
# ---------------------------------------------------------------------------

def _prepare_output_directory() -> None:
    """Create the plot output directory if it does not already exist."""

    PLOTS_DIR.mkdir(parents=True, exist_ok=True)


def _save_figure(fig: plt.Figure, filename: str) -> Path:
    """Save a figure to the project's results/plots directory.

    Parameters
    ----------
    fig:
        Matplotlib figure to save.

    filename:
        Output filename, including the file extension.

    Returns
    -------
    pathlib.Path
        Path to the saved figure.
    """

    _prepare_output_directory()

    output_path = PLOTS_DIR / filename

    fig.savefig(
        output_path,
        dpi=300,
        bbox_inches="tight",
    )

    return output_path


def _format_axis(ax: plt.Axes) -> None:
    """Apply consistent formatting to a plot axis."""

    ax.tick_params(axis="both", labelsize=TICK_LABEL_SIZE)

    ax.yaxis.offsetText.set_fontsize(OFFSET_TEXT_SIZE)

    ax.set_xlim(0, MAX_GENERATION)


# ---------------------------------------------------------------------------
# GA results data
# ---------------------------------------------------------------------------

def plot_ga_fitness(
    data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot optimisation fitness against generation.

    The figure contains:

    * the highest fitness achieved in each generation;
    * a moving average of average fitness;
    * individual fitness values.

    Parameters
    ----------
    data:
        GA results dataset.

    save:
        If True, save the figure to ``results/plots/``.

    Returns
    -------
    matplotlib.figure.Figure
        The generated figure.
    """

    generations = data["Generation"]
    best_fp = data["Highest Fp"]
    average_fp = data["Average Fp"]
    iterations = data["Iteration"]
    individual_fp = data["Fp"]

    moving_average = average_fp.rolling(
        window=MOVING_AVERAGE_WINDOW,
        min_periods=1,
    ).mean()

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        generations,
        best_fp,
        color=PRIMARY_COLOUR,
        label="Highest Fp",
    )

    ax.plot(
        generations,
        moving_average,
        color=SECONDARY_COLOUR,
        label=f"Average Fp ({MOVING_AVERAGE_WINDOW}-point moving average)",
    )

    ax.scatter(
        iterations,
        individual_fp,
        alpha=0.3,
        color=INDIVIDUAL_COLOUR,
        s=1,
        label="Individual Fp",
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel(r"Q / V  [m$^{-3}$]", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    ax.legend(frameon=False)

    fig.tight_layout()

    if save:
        _save_figure(fig, "ga_fitness.png")

    return fig


# ---------------------------------------------------------------------------
# GA logs data
# ---------------------------------------------------------------------------

def plot_resonant_frequency(
    log_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot resonant frequency against generation."""

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        log_data["Generation"],
        log_data["Fcalc"],
        color=PRIMARY_COLOUR,
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel("Resonant Frequency  [GHz]", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    fig.tight_layout()

    if save:
        _save_figure(fig, "resonant_frequency.png")

    return fig


def plot_q_factor(
    log_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot Q factor against generation."""

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        log_data["Generation"],
        log_data["Qfactor"],
        color=PRIMARY_COLOUR,
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel("Q Factor", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    fig.tight_layout()

    if save:
        _save_figure(fig, "q_factor.png")

    return fig


def plot_mode_volume(
    log_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot mode volume against generation."""

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        log_data["Generation"],
        log_data["Vm"],
        color=SECONDARY_COLOUR,
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel(r"Mode volume  [m$^{-3}$]", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    fig.tight_layout()

    if save:
        _save_figure(fig, "mode_volume.png")

    return fig


# ---------------------------------------------------------------------------
# Spurious-mode analysis
# ---------------------------------------------------------------------------

def plot_spurious_fitness(
    spurious_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot optimisation fitness for the spurious-mode dataset.

    This plot uses the same visualisation approach as the GA
    fitness plot, allowing the two optimisation runs to be compared.
    """

    generations = spurious_data["Generation"]
    best_fp = spurious_data["Highest Fp"]
    average_fp = spurious_data["Average Fp"]

    moving_average = average_fp.rolling(
        window=MOVING_AVERAGE_WINDOW,
        min_periods=1,
    ).mean()

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        generations,
        best_fp,
        color=PRIMARY_COLOUR,
        label="Highest Fp",
    )

    ax.plot(
        generations,
        moving_average,
        color=SECONDARY_COLOUR,
        label=f"Average Fp ({MOVING_AVERAGE_WINDOW}-point moving average)",
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel(r"Q / V  [m$^{-3}$]", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    ax.legend(frameon=False)

    fig.tight_layout()

    if save:
        _save_figure(fig, "spurious_fitness.png")

    return fig


def plot_spurious_fitness_zoomed(
    spurious_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot a zoomed view of spurious-mode optimisation fitness.

    The y-axis is limited to 0–1e11 and the generation axis to 0–225,
    matching the corresponding exploratory plot from the notebook.
    """

    generations = spurious_data["Generation"]
    best_fp = spurious_data["Highest Fp"]
    average_fp = spurious_data["Average Fp"]

    moving_average = average_fp.rolling(
        window=MOVING_AVERAGE_WINDOW,
        min_periods=1,
    ).mean()

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        generations,
        best_fp,
        color=PRIMARY_COLOUR,
        label="Highest Fp",
    )

    ax.plot(
        generations,
        moving_average,
        color=SECONDARY_COLOUR,
        label=f"Average Fp ({MOVING_AVERAGE_WINDOW}-point moving average)",
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel(r"Q / V  [m$^{-3}$]", fontsize=Y_LABEL_SIZE)

    ax.set_xlim(0, 225)
    ax.set_ylim(0, 1e11)

    ax.tick_params(axis="both", labelsize=TICK_LABEL_SIZE)
    ax.yaxis.offsetText.set_fontsize(OFFSET_TEXT_SIZE)

    ax.legend(frameon=False)

    fig.tight_layout()

    if save:
        _save_figure(fig, "spurious_fitness_zoomed.png")

    return fig


# ---------------------------------------------------------------------------
# Permittivity analysis
# ---------------------------------------------------------------------------

def plot_permittivity(
    permittivity_data: pd.DataFrame,
    save: bool = True,
) -> plt.Figure:
    """Plot permittivity against generation."""

    fig, ax = plt.subplots(figsize=FIGURE_SIZE)

    ax.plot(
        permittivity_data["Generation"],
        permittivity_data["Permittivity"],
        color=SECONDARY_COLOUR,
    )

    ax.set_xlabel("Generation", fontsize=X_LABEL_SIZE)
    ax.set_ylabel("Permittivity", fontsize=Y_LABEL_SIZE)

    _format_axis(ax)

    fig.tight_layout()

    if save:
        _save_figure(fig, "permittivity.png")

    return fig


# ---------------------------------------------------------------------------
# Complete exploratory analysis
# ---------------------------------------------------------------------------

def generate_all_plots() -> dict[str, plt.Figure]:
    """Generate all exploratory analysis plots.

    The four datasets are loaded from ``results/`` and all figures are
    saved to ``results/plots/``.

    Returns
    -------
    dict[str, matplotlib.figure.Figure]
        Dictionary containing all generated figures.
    """

    configure_plot_style()

    data = load_all_data()

    figures = {
        "ga_fitness": plot_ga_fitness(
            data["results"]
        ),
        "resonant_frequency": plot_resonant_frequency(
            data["logs"]
        ),
        "q_factor": plot_q_factor(
            data["logs"]
        ),
        "mode_volume": plot_mode_volume(
            data["logs"]
        ),
        "spurious_fitness": plot_spurious_fitness(
            data["spurious"]
        ),
        "spurious_fitness_zoomed": plot_spurious_fitness_zoomed(
            data["spurious"]
        ),
        "permittivity": plot_permittivity(
            data["permittivity"]
        ),
    }

    return figures