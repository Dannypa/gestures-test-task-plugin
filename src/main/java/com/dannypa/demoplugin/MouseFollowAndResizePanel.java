package com.dannypa.demoplugin;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Enum that represents the four sides that the panel has.
 */
enum Side {
    TOP, RIGHT, BOTTOM, LEFT
}

/**
 * The panel that records when mouse enters and the side it enters from.
 * Draws the passed component and makes it follow the mouse such that mouse is at the center of the component.
 * Resizes the component based on the mouse movement: the dimensions are updated by the formula
 * currentD = min(originalD / b + k * distance(mouse, entranceSide), originalD)
 * where:
 * <ul>
 *     <li>originalD - the original dimension of the component</li>
 *     <li>b - the initial scaling factor</li>
 *     <li>k - a scaling factor to adjust sensitivity</li>
 *     <li>distance(mouse, entranceSide) - the distance from the mouse to the side of the panel it entered from</li>
 * </ul>
 */
public class MouseFollowAndResizePanel extends JPanel {

    /**
     * Initial scaling factor b from the formula above.
     */
    private final int INITIAL_SCALE = 2;

    /**
     * Scaling factor k from the formula above.
     */
    private final double SCALING_FACTOR = 0.001;

    /**
     * The side from which the mouse entered last time.
     */
    private Side entranceSide;

    /**
     * The original size of the component.
     */
    private final Dimension originalSize;

    /**
     * Generates a mouse input adapter that handles mouse events such as entering, exiting, and movement.
     * <p>
     * The adapter:
     * <ul>
     *     <li>Tracks the side where the mouse enters.</li>
     *     <li>Updates the component size and visibility.</li>
     *     <li>Centers the component on the mouse location.</li>
     * </ul>
     *
     * @param component The component to be resized and moved.
     * @return A {@link MouseInputAdapter} to handle mouse events.
     */
    private MouseInputAdapter getMouseInputAdapter(Component component) {
        return new MouseInputAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                entranceSide = getClosestSide(e.getLocationOnScreen());
                component.setVisible(true);
                component.setSize(new Dimension(
                        originalSize.width / INITIAL_SCALE,
                        originalSize.height / INITIAL_SCALE
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                component.setVisible(false);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                assert entranceSide != null;

                int distanceToSide = getDistanceToSide(e.getLocationOnScreen(), entranceSide);
                component.setSize(calculateCurrentComponentSize(distanceToSide));
                centerComponentAtMouse(component, e.getPoint());
                component.revalidate();
            }
        };
    }

    /**
     * Centers the component relative to the mouse location.
     *
     * @param component     The component to be centered.
     * @param mousePosition The current mouse position.
     */
    private void centerComponentAtMouse(Component component, Point mousePosition) {
        mousePosition.x -= component.getSize().width / 2;
        mousePosition.y -= component.getSize().height / 2;
        component.setLocation(mousePosition);
    }

    /**
     * Constructs a {@link MouseFollowAndResizePanel} that tracks mouse movement, entrance, and resizing.
     *
     * @param component     The component to be resized and moved based on mouse interaction.
     * @param componentSize The original size of the component before any scaling.
     */
    public MouseFollowAndResizePanel(Component component, Dimension componentSize) {
        this.setLayout(null);
        this.add(component);
        originalSize = componentSize;
        MouseInputAdapter handler = getMouseInputAdapter(component);
        this.addMouseListener(handler);
        this.addMouseMotionListener(handler);
    }

    /**
     * Applies the scaling formula to a number (component dimension).
     *
     * @param original       Original dimension.
     * @param distanceToSide Current distance from mouse to the side of the panel it entered from.
     * @return The result of the scaling of the original dimension given by the formula in the class description.
     */
    private int calculateCurrentComponentDimension(int original, int distanceToSide) {
        return Math.min(
                original / INITIAL_SCALE + (int) ((distanceToSide * SCALING_FACTOR) * original),
                original
        );
    }

    /**
     * Applies the scaling formula to get the current component size.
     *
     * @param distanceToSide Current distance from mouse to the side of the panel it entered from.
     * @return The result of scaling of the component size given by the formula in the class description.
     */
    private Dimension calculateCurrentComponentSize(int distanceToSide) {
        return new Dimension(
                calculateCurrentComponentDimension(originalSize.width, distanceToSide),
                calculateCurrentComponentDimension(originalSize.height, distanceToSide)
        );
    }

    /**
     * Retrieves the coordinate of the specified side of the panel.
     *
     * @param side The {@link Side} for which the coordinate is required.
     * @return The relevant coordinate of the panel's side.
     * If the side is TOP or BOTTOM, returns its y; otherwise returns its x.
     */
    private int getPanelSide(Side side) {
        // if side in {top, bottom} returns y; otherwise returns x
        Point panelLocation = getLocationOnScreen();
        Dimension panelSize = getSize();

        if (side == Side.TOP) return panelLocation.y;
        else if (side == Side.RIGHT) return panelLocation.x + panelSize.width;
        else if (side == Side.BOTTOM) return panelLocation.y + panelSize.height;
        else return panelLocation.x;
    }

    /**
     * @param p    The point whose coordinate is returned.
     * @param side The side that we compute the coordinate for.
     * @return The coordinate relevant for this side.
     * If the side is TOP or BOTTOM, returns its y; otherwise returns its x.
     */
    private int getRelevantCoordinate(Point p, Side side) {
        if (side == Side.TOP || side == Side.BOTTOM) {
            return p.y;
        } else {
            return p.x;
        }
    }

    /**
     * @param p The point which we compute the distance from.
     * @return The {@link Side} closest to the specified point.
     */
    private Side getClosestSide(Point p) {
        // not always clear: what if it is diagonal?
        // for now let's just return something; may be improved later

        int[][] distances = new int[][]{
                new int[]{Math.abs(getPanelSide(Side.TOP) - p.y), 0}, // top
                new int[]{Math.abs(getPanelSide(Side.RIGHT) - p.x), 1}, // right
                new int[]{Math.abs(getPanelSide(Side.BOTTOM) - p.y), 2}, // bottom
                new int[]{Math.abs(getPanelSide(Side.LEFT) - p.x), 3}, // left
        };
        Arrays.sort(distances, Comparator.comparingInt(a -> a[0]));

        return Side.values()[distances[0][1]];
    }

    /**
     * @param p    The point which we compute the distance from.
     * @param side The {@link Side} to calculate the distance to.
     * @return The distance from the point to the panel's side.
     */
    private int getDistanceToSide(Point p, Side side) {
        return Math.abs(getRelevantCoordinate(p, side) - getPanelSide(side));
    }
}
