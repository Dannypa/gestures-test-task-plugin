package com.dannypa.demoplugin;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A class that performs a preprocessing task in the background,
 * Displays a progress bar during computation, and shows the result after completion.
 * This class uses SwingWorker as it works robustly with JProgressBar.
 */
public class PreprocessingManager implements PropertyChangeListener {
    /**
     * Background task that performs the computation.
     */
    private final SwingWorker<Void, Void> task;

    /**
     * Progress bar that shows computation progress.
     */
    private final JProgressBar progressBar;

    /**
     * Function to display the final result panel.
     */
    private final Runnable setUpResultPanel;

    /**
     * Function to disable the progress bar panel.
     */
    private final Runnable disableProgressBarPanel;

    /**
     * Label to display the result text.
     */
    private final JLabel resultLabel;

    /**
     * Property change handler to react to updating progress of the background task and
     * perform the final operation after the task is done.
     * @param propertyChangeEvent The event containing the property change.
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            progressBar.setValue(task.getProgress());
        } else if (propertyChangeEvent.getPropertyName().equals("state")
                && propertyChangeEvent.getNewValue() == SwingWorker.StateValue.DONE) {
            disableProgressBarPanel.run();

            resultLabel.setText("Loaded!");
            // the proper way would be to make the class generic and then have `private T result` and use it here
            // but i am just doing proof of concept here.
            setUpResultPanel.run();
        }
    }

    /**
     * Starts the preprocessing task in the background.
     * NB!!! all the fields should be defined before calling this method!!!
     */
    public void startPreprocessing() {
        task.addPropertyChangeListener(this);
        task.execute();
    }

    /**
     * Constructs the PreprocessingBackend, initializes the progress bar,
     * and starts the background computation task.
     *
     * @param progressBar             The progress bar to display task progress.
     * @param showProgressBarPanel    Runnable that configures the progress bar panel and makes it visible
     * @param disableProgressBarPanel Runnable to hide the progress bar panel after completion.
     * @param resultLabel             The label to display the final result.
     * @param showResultPanel         Runnable that configures the result panel and makes it visible
     */
    PreprocessingManager(
            JProgressBar progressBar,
            Runnable showProgressBarPanel,
            Runnable disableProgressBarPanel,
            JLabel resultLabel,
            Runnable showResultPanel,
            SwingWorker<Void, Void> task
    ) {
        this.task = task;
        this.progressBar = progressBar;
        this.resultLabel = resultLabel;
        this.setUpResultPanel = showResultPanel;
        this.disableProgressBarPanel = disableProgressBarPanel;

        progressBar.setValue(0);
        showProgressBarPanel.run();
    }
}

