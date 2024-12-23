package com.dannypa.demoplugin;

import javax.swing.*;
import java.awt.*;

public class PreprocessingPanel extends JPanel {
    private static final Font PROGRESS_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 40);
    private static final Font RESULT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 60);

    /**
     * Panel to display the progress bar.
     */
    private final JPanel progressBarPanel = new JPanel();

    /**
     * Progress bar that shows computation progress.
     */
    private final JProgressBar progressBar = new JProgressBar();

    /**
     * Panel to display the final result.
     */
    private final JPanel resultPanel = new JPanel();

    /**
     * Label to display the result text.
     */
    private final JLabel resultLabel = new JLabel();


    /**
     * Creates a GridBagConstraints object for components based on weightx and weighty.
     *
     * @param weightX Horizontal weight of the component; see {@link GridBagConstraints#weightx}.
     * @param weightY Vertical weight of the component; see {@link GridBagConstraints#weighty}.
     * @return A configured GridBagConstraints object.
     */
    private GridBagConstraints getNoneFillGBC(int weightX, int weightY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        return gbc;
    }

    /**
     * Adds a component to a panel using the specified GridBagConstraints.
     *
     * @param panel The target panel.
     * @param c     The component to add.
     * @param gbc   The GridBagConstraints for the component.
     */
    private void addComponent(JPanel panel, Component c, GridBagConstraints gbc) {
        panel.add(c, gbc);
//        JPanel debug = new JPanel();
//        debug.setSize(100, 100);
//        debug.setBorder(BorderFactory.createLineBorder(Color.RED, 10));
//        panel.add(debug, gbc);
    }

    /**
     * Adds a row of components to a panel with specified constraints.
     *
     * @param panel       The target panel.
     * @param rowIndex    The row index to add components.
     * @param components  Array of components to add.
     * @param constraints Array of GridBagConstraints for each component.
     */
    private void addRow(JPanel panel, int rowIndex, Component[] components, GridBagConstraints[] constraints) {
        assert components.length == constraints.length;
        for (int i = 0; i < components.length; i++) {
            constraints[i].gridx = i;
            constraints[i].gridy = rowIndex;
            addComponent(panel, components[i], constraints[i]);
        }
    }

    /**
     * Centers a component within a panel using GridBagLayout.
     *
     * @param panel             The target panel.
     * @param component         The component to center.
     * @param verticalWeights   Weights for vertical layout.
     * @param horizontalWeights Weights for horizontal layout.
     */
    private void centerComponent(JPanel panel, Component component, int[] verticalWeights, int[] horizontalWeights) {
        addRow(
                panel,
                0,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getNoneFillGBC(1, verticalWeights[0])}
        );

        GridBagConstraints[] middleRowConstraints = new GridBagConstraints[3];
        for (int i = 0; i < 3; i++) {
            middleRowConstraints[i] = getNoneFillGBC(horizontalWeights[i], 1);
        }
        middleRowConstraints[1].fill = GridBagConstraints.BOTH;
        addRow(
                panel,
                1,
                new Component[]{Box.createHorizontalGlue(), component, Box.createHorizontalGlue()},
                middleRowConstraints
        );

        addRow(
                panel,
                2,
                new Component[]{Box.createVerticalGlue()},
                new GridBagConstraints[]{getNoneFillGBC(1, verticalWeights[2])}
        );
    }

    /**
     * Configures the progress bar UI.
     */
    private void setUpProgressBarUI() {
        progressBar.setStringPainted(true);
        progressBar.setFont(PROGRESS_FONT);
    }

    /**
     * Configures the result label UI to display the computation result.
     */
    private void setUpResultLabelUI() {
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setVerticalAlignment(JLabel.CENTER);
        resultLabel.setFont(RESULT_FONT);
    }


    /**
     * Sets up a panel and centers a given component in it using specified layout weights.
     *
     * @param panel             The target panel.
     * @param component         The component to center.
     * @param verticalWeights   Weights for vertical layout.
     * @param horizontalWeights Weights for horizontal layout.
     */
    private void setUpPanel(JPanel panel, Component component, int[] verticalWeights, int[] horizontalWeights) {
        panel.setLayout(new GridBagLayout());
        centerComponent(panel, component, verticalWeights, horizontalWeights);
        panel.revalidate();
    }


    /**
     * Creates the panel. Configures the UI using the GridBagLayout and starts the preprocessing.
     */
    PreprocessingPanel(SwingWorker<Void, Void> task) {
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));

        PreprocessingManager preprocessing = new PreprocessingManager(
                progressBar,
                () -> {
                    setUpProgressBarUI();
                    setUpPanel(progressBarPanel, progressBar, new int[]{3, 1, 3}, new int[]{1, 4, 1});
                    this.add(progressBarPanel);
                },
                () -> {
                    this.remove(progressBarPanel);
                    this.revalidate();
                },
                resultLabel,
                () -> {
                    setUpResultLabelUI();
                    setUpPanel(resultPanel, resultLabel, new int[]{3, 1, 3}, new int[]{1, 4, 1});
                    this.add(resultPanel);
                },
                task
        );

        preprocessing.startPreprocessing();
    }
}
