package lab5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

import lab5.fractal.*;

public class FractalExplorer {
    
    /**
     *  the width and height of the display in pixels. (Our fractal display will be square.)
     */
    private int _displaySize;
    
    /**
     *  
     */
    private int _rowsRemaining;
    
    /**
     *   reference, so that we can update our display from various methods, as we compute the fractal
     */
    private JImageDisplay _image;
    
    /**
     *   support for multiple fractals
     */
    private JComboBox<String> _fractalChooser;
    
    /**
     *   FractalExplorer Save button
     */
    private JButton _saveButton;
    
    /**
     *   FractalExplorer Reset button
     */
    private JButton _resetButton;
    
    /**
     *   base-class reference so that we can show other kinds of fractals in the future
     */
    private FractalGenerator _gen;
    
    /**
     *  specifying the range of the complex plane that we are currently displaying
     */
    Rectangle2D.Double _range;

    private class FractalHandler implements ActionListener 
    { 
        public void actionPerformed(ActionEvent e) 
        { 
            String cmd = e.getActionCommand(); 

            if (e.getSource() == _fractalChooser) 
            { 
                String selectedItem = _fractalChooser.getSelectedItem().toString();

                if(selectedItem.equals(Mandelbrot.nameString()))
                {
                    _gen = new Mandelbrot();
                }
                else if(selectedItem.equals(Tricorn.nameString()))
                {
                    _gen = new Tricorn();
                }
                else if(selectedItem.equals(BurningShip.nameString()))
                {
                    _gen = new BurningShip();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Error: fractalChooser unknown choice");
                    return;
                }
                
                _range = new Rectangle2D.Double();
                _gen.getInitialRange(_range);
                
                drawFractal();
            } 
            else if (cmd.equals("reset")) 
            { 
                _range = new Rectangle2D.Double();
                _gen.getInitialRange(_range);
                
                drawFractal();
            } 
            else if (cmd.equals("save")) 
            { 
                JFileChooser chooser = new JFileChooser();
                
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                chooser.setFileFilter(filter);
                chooser.setAcceptAllFileFilterUsed(false);
                
                if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
                {
                    try 
                    {
                        File fd = chooser.getSelectedFile();
                        String filePath = fd.getPath();
                        if(!filePath.toLowerCase().endsWith(".png"))
                        {
                            fd = new File(filePath + ".png");
                        }
                        
                        ImageIO.write(_image.getImage(), "png", fd);
                    } 
                    catch (IOException exc) 
                    {
                        JOptionPane.showMessageDialog(null, "Error: couldn't save file ( " + exc.getMessage() + " )");
                        
                        exc.printStackTrace();
                    }
                }
            } 
            else
            {
                JOptionPane.showMessageDialog(null, "Error: FractalHandler unknown action");
            }
        } 
    }
    
    private class MouseHandler extends MouseAdapter 
    { 
        public void mouseClicked(MouseEvent e)
        { 
            if(_rowsRemaining > 0){
                return;
            }
            
            double xCoord = getFractaleXcoord(e.getX());
            double yCoord = getFractaleYcoord(e.getY());
            
            _gen.recenterAndZoomRange(_range,xCoord, yCoord, 0.5);
            
            drawFractal();
        } 
    }
    
    private class FractalWorker extends SwingWorker<Object, Object> {
        
        /**
         *  y-coordinate of the row that will be computed
         */
        private int _y;
        
        /**
         *  hold the computed RGB values for each pixel in that row
         */
        private int[] _rgbVals;
        
        public FractalWorker(int y)
        {
            _y = y;
        }
        
        /**
         *  store each RGB value into the corresponding element of the integer array
         */
        protected Object doInBackground()
        {
            _rgbVals = new int[_displaySize];
            
            int numIters;
            int rgbColor;
            
            double yCoord = getFractaleYcoord(_y);
            double xCoord;
            float hue;
            
            for(int x = 0 ; x < _displaySize ; ++x)
            {
                xCoord = getFractaleXcoord(x);
                
                numIters = _gen.numIterations(xCoord, yCoord);
                if(numIters < 0)
                {
                    rgbColor = 0;
                }
                else
                {
                    hue = 0.7f + numIters / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                
                _rgbVals[x] = rgbColor;
            }
            
            return null;
        }
        
        protected void done()
        {
            for(int x = 0 ; x < _displaySize ; ++x)
            {
                _image.drawPixel(x, _y, _rgbVals[x]);
            }
            
            _image.repaint(0, 0, _y, _displaySize, 1);
            
            if((--_rowsRemaining) < 1)
            {
                enableUI(true);
            }
        }
        
    }
    
    public FractalExplorer(int displaySize)
    {
        _displaySize = displaySize;
        
        _gen = new Mandelbrot();
        
        _range = new Rectangle2D.Double();
        _gen.getInitialRange(_range);
    }
    
    public void createAndShowGUI()
    {
        JFrame frame = new JFrame("Fractal Explorer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();

        contentPane.setLayout(new BorderLayout());
        
        FractalHandler handler = new FractalHandler();
        
        /**
         * Choose Fractal Panel
         */
        JPanel fractalPanel = new JPanel();
        
        JLabel panelLabel = new JLabel("Fractal: ");
        fractalPanel.add(panelLabel);
        
        _fractalChooser = new JComboBox<String>();
        _fractalChooser.addItem(Mandelbrot.nameString());
        _fractalChooser.addItem(Tricorn.nameString());
        _fractalChooser.addItem(BurningShip.nameString());
        _fractalChooser.addActionListener(handler);
        
        fractalPanel.add(_fractalChooser);
        
        contentPane.add(fractalPanel, BorderLayout.NORTH);

        /**
         * image
         */
        _image = new JImageDisplay(_displaySize, _displaySize);
        contentPane.add(_image, BorderLayout.CENTER);
        
        /**
         * Choose Fractal Panel
         */
        JPanel buttonsPanel = new JPanel();
        
        /**
         * save button
         */
        _saveButton = new JButton("Save Image");
        _saveButton.setActionCommand("save"); 
        _saveButton.addActionListener(handler);
        buttonsPanel.add(_saveButton);
        
        /**
         * reset button
         */
        _resetButton = new JButton("Reset Display");
        _resetButton.setActionCommand("reset"); 
        _resetButton.addActionListener(handler);
        buttonsPanel.add(_resetButton);
        
        contentPane.add(buttonsPanel, BorderLayout.SOUTH);
        
        contentPane.addMouseListener(new MouseHandler());
        
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }
    
    public void drawFractal()
    {
        this.enableUI(false);
        _rowsRemaining = _displaySize;
        
        for(int y = 0 ; y < _displaySize ; ++y)
        {
            FractalWorker worker = new FractalWorker(y);
            worker.execute();
        }
        
        _image.repaint();
    }
    
    private void enableUI(boolean val)
    {
        _fractalChooser.setEnabled(val);
        
        _saveButton.setEnabled(val);
        _resetButton.setEnabled(val);
    }
    
    /**
     * @param x the pixel-coordinate
     * @return the coordinate in the fractal's space
     */
    private double getFractaleXcoord(int x)
    {
        return FractalGenerator.getCoord(_range.x, _range.x + _range.width, _displaySize, x);
    }
    
    /**
     * @param y the pixel-coordinate
     * @return the coordinate in the fractal's space
     */
    private double getFractaleYcoord(int y)
    {
        return FractalGenerator.getCoord(_range.y, _range.y + _range.height, _displaySize, y);
    }
    
    /**
     * Entry-point for the application.  No command-line arguments are
     * recognized at this time.
     **/
    public static void main(String[] args) 
    {
        FractalExplorer explorer = new FractalExplorer (400);
        explorer.createAndShowGUI();
        explorer.drawFractal();
    } 
}
