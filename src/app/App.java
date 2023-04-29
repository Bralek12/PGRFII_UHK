package app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import raster.CropView;
import render.Renderer;
import solids.Axis;
import solids.Cube;
import solids.CubicObject;
import solids.CubicObjectPoints;
import solids.Pyramid;
import solids.Solid;
import transforms.Camera;
import transforms.Col;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Mat4OrthoRH;
import transforms.Mat4PerspRH;
import transforms.Mat4RotX;
import transforms.Mat4RotY;
import transforms.Mat4Scale;
import transforms.Mat4Transl;
import transforms.Point3D;
import transforms.Vec3D;

public class App {

	private CropView testVisibility = new CropView(800, 600);
	private Renderer renderer = new Renderer(testVisibility);
	private Mat4 model = new Mat4Identity();
	private Camera camera = new Camera(new Vec3D(-1, -6, 3), Math.toRadians(70), Math.toRadians(-10), 1, true);
	private int xStart, yStart;
	private List<Solid> solids;
	private JFrame window;
	private JPanel panelView;
	private boolean changeProjection = true;
	private boolean wireframe = false;
	private BufferedImage bufferedImage;

	public App() {
		// Initialize window
		window = new JFrame();
		bufferedImage = testVisibility.getBufferedImage();
		renderer.setView(camera.getViewMatrix());
		panelView = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bufferedImage, 0, 0, null);
			}
		};

		// Add object to solids
		solids = new ArrayList<>();
		Solid cube = new Cube();
		solids.add(cube);
		Solid pyramid = new Pyramid();
		solids.add(pyramid);
		Solid cubic = new CubicObject();
		solids.add(cubic);
		Solid cubicPoints = new CubicObjectPoints();
		solids.add(cubicPoints);

		// Add axis to solids
		solids.add(new Axis(new Point3D(-0.5, 0, 3.5), new Point3D(0.5, 0, 3.5), new Point3D(0, 0, 4),
				new Col(0, 0, 255)));
		solids.add(new Axis(new Point3D(-0.5, 3.5, 0), new Point3D(0.5, 3.5, 0), new Point3D(0, 4, 0),
				new Col(0, 255, 0)));
		solids.add(new Axis(new Point3D(3.5, -0.5, 0), new Point3D(3.5, 0.5, 0), new Point3D(4, 0, 0),
				new Col(255, 0, 0)));

		// Add solids to renderer
		panelView.setPreferredSize(new Dimension(testVisibility.getWidth(), testVisibility.getHeight()));
		panelView.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xStart = e.getX();
				yStart = e.getY();
			}
		});

		// Mouse dragged listener
		panelView.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				int xEnd = e.getX();
				int yEnd = e.getY();
				if (SwingUtilities.isLeftMouseButton(e)) {
					double azimuth = camera.getAzimuth();
					double zenith = camera.getZenith();
					if (zenith < (-Math.PI / 2) && zenith > (Math.PI / 2)) {
						zenith = Math.PI / 2;
					}
					if (azimuth < (-Math.PI) && azimuth > (Math.PI)) {
						azimuth = Math.PI;
					}
					camera = camera.withAzimuth(azimuth + ((xStart - xEnd) * Math.PI) / bufferedImage.getWidth());
					camera = camera.withZenith(zenith + ((yStart - yEnd) * Math.PI) / bufferedImage.getHeight());
					renderer.setView(camera.getViewMatrix());
					renderObjects();
					panelView.repaint();
				} else if (SwingUtilities.isRightMouseButton(e)) {
					double dx = (xStart - xEnd) * Math.PI / bufferedImage.getWidth();
					double dy = (yStart - yEnd) * Math.PI / bufferedImage.getHeight();
					if (Math.abs(dx) > Math.abs(dy)) {
						if (dx > 0) {
							model = model.mul(new Mat4RotX(-0.01));
						} else {
							model = model.mul(new Mat4RotX(0.01));
						}
					} else {
						if (dy > 0) {
							model = model.mul(new Mat4RotY(-0.01));
						} else {
							model = model.mul(new Mat4RotY(0.01));
						}
					}
					renderer.setModel(model);
					renderObjects();
					panelView.repaint();
				}
				xStart = e.getX();
				yStart = e.getY();
			}
		});

		// Add mouse wheel listener for zoom
		panelView.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int rotation = e.getWheelRotation();
				if (rotation < 0) {
					model = model.mul(new Mat4Scale(1.1));
				} else {
					model = model.mul(new Mat4Scale(0.9));
				}
				renderer.setModel(model);
				renderObjects();
				panelView.repaint();
			}
		});

		// Add key listener for camera movement
		window.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (!e.isControlDown()) {
					if (e.getKeyCode() == KeyEvent.VK_W) {
						camera = camera.forward(0.05);
					} else if (e.getKeyCode() == KeyEvent.VK_S) {
						camera = camera.backward(0.05);
					} else if (e.getKeyCode() == KeyEvent.VK_A) {
						camera = camera.left(0.05);
					} else if (e.getKeyCode() == KeyEvent.VK_D) {
						camera = camera.right(0.05);
					} else if (e.getKeyCode() == KeyEvent.VK_Q) {
						camera = camera.up(0.05);
					} else if (e.getKeyCode() == KeyEvent.VK_E) {
						camera = camera.down(0.05);
					}
					renderer.setView(camera.getViewMatrix());
					renderObjects();
				} else {
					if (e.getKeyCode() == KeyEvent.VK_W) {
						model = model.mul(new Mat4Transl(-0.1, 0, 0));
					} else if (e.getKeyCode() == KeyEvent.VK_S) {
						model = model.mul(new Mat4Transl(0.1, 0, 0));
					} else if (e.getKeyCode() == KeyEvent.VK_Q) {
						model = model.mul(new Mat4Transl(0, 0, 0.1));
					} else if (e.getKeyCode() == KeyEvent.VK_Z) {
						model = model.mul(new Mat4Transl(0, 0, -0.1));
					} else if (e.getKeyCode() == KeyEvent.VK_A) {
						model = model.mul(new Mat4Transl(0, -0.1, 0));
					} else if (e.getKeyCode() == KeyEvent.VK_D) {
						model = model.mul(new Mat4Transl(0, 0.1, 0));
					}
					renderer.setModel(model);
					renderObjects();
				}
				panelView.repaint();
			}
		});

		// Window setting
		window.setTitle("PGRF2 - Uloha 1");
		window.setResizable(false);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Add info panel
		JPanel panelInfo = new JPanel();
		panelInfo.setPreferredSize(new Dimension(200, 30));
		panelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		JLabel infoWSADQE = new JLabel(
				"WSADQE - pohyb kamery | Levé tlačítko myši - otáčení kamery | kolečko myši - zoom");
		panelInfo.add(infoWSADQE);

		// Add control panel
		JPanel panelControl = new JPanel();
		panelControl.setPreferredSize(new Dimension(200, 50));
		panelControl.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panelControl.add(Box.createVerticalGlue()); // Add a vertical glue at the top
		Dimension buttonSize = new Dimension(120, 30);

		// Add button for WireFrame
		JButton button1 = new JButton("WireFrame");
		button1.setFocusable(false);
		button1.setPreferredSize(buttonSize);
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (wireframe) {
					renderer.setWireframe(false);
					renderObjects();
					wireframe = false;
					button1.setText("WireFrame");
				} else {
					renderer.setWireframe(true);
					renderObjects();
					wireframe = true;
					button1.setText("Fill");
				}
			}
		});
		panelControl.add(button1);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10))); // Add some vertical space between the buttons

		// Add button for WireFrame
		JButton button2 = new JButton("Orthogonal");
		button2.setFocusable(false);
		button2.setPreferredSize(new Dimension(buttonSize));
		button2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (changeProjection) {
					renderer.setProjection(new Mat4OrthoRH(5, 5, 1, 200));
					changeProjection = false;
					renderObjects();
					button2.setText("Perspective");
				} else {
					renderer.setProjection(
							new Mat4PerspRH(Math.PI / 4, testVisibility.getWidth() / testVisibility.getHeight(), 1,
									200));
					changeProjection = true;
					renderObjects();
					button2.setText("Orthogonal");
				}
			}
		});
		panelControl.add(button2);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add button for Cube toggle
		JButton button3 = new JButton("Cube Off");
		button3.setFocusable(false);
		button3.setPreferredSize(new Dimension(buttonSize));
		button3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solids.contains(cube)) {
					solids.removeIf(solid -> solid instanceof Cube);
					button3.setText("Cube On");
				} else {
					solids.add(cube);
					button3.setText("Cube Off");
				}
				renderObjects();
			}
		});
		panelControl.add(button3);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add button for Pyramid toggle
		JButton button4 = new JButton("Pyramid Off");
		button4.setFocusable(false);
		button4.setPreferredSize(new Dimension(buttonSize));
		button4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solids.contains(pyramid)) {
					solids.removeIf(solid -> solid instanceof Pyramid);
					button4.setText("Pyramid On");
				} else {
					solids.add(pyramid);
					button4.setText("Pyramid Off");
				}
				renderObjects();
			}
		});
		panelControl.add(button4);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add button for Cubic Object toggle
		JButton button5 = new JButton("Cubic Off");
		button5.setFocusable(false);
		button5.setPreferredSize(new Dimension(buttonSize));
		button5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solids.contains(cubic)) {
					solids.removeIf(solid -> solid instanceof CubicObject);
					button5.setText("Cubic On");
				} else {
					solids.add(cubic);
					button5.setText("Cubic Off");
				}
				renderObjects();
			}
		});
		panelControl.add(button5);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10)));

		// Add button for Cubic Object toggle
		JButton button6 = new JButton("Points Off");
		button6.setFocusable(false);
		button6.setPreferredSize(new Dimension(buttonSize));
		button6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solids.contains(cubicPoints)) {
					solids.removeIf(solid -> solid instanceof CubicObjectPoints);
					button6.setText("Points On");
				} else {
					solids.add(cubicPoints);
					button6.setText("Points Off");
				}
				renderObjects();
			}
		});
		panelControl.add(button6);
		panelControl.add(Box.createRigidArea(new Dimension(0, 10)));

		// Finish window
		window.add(panelInfo, BorderLayout.NORTH);
		window.add(panelControl, BorderLayout.SOUTH);
		window.add(panelView, BorderLayout.CENTER);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		renderObjects();
	}

	private void renderObjects() {
		testVisibility.clear(0x000000);
		renderer.render(solids);
		panelView.repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new App());
	}
}
