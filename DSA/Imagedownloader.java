import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Imagedownloader extends JFrame {

    private JTextField urlField;
    private JButton downloadButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JButton cancelButton;
    private JButton resetButton; 
    private JLabel statusLabel;
    private JPanel imagePanel;
    private JProgressBar progressBar;

    private ExecutorService threadPool;
    private List<Future<?>> downloadTasks;
    private Lock lock = new ReentrantLock();

    public Imagedownloader() {
        super("Image Downloader");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        urlField = new JTextField();
        downloadButton = new JButton("Download");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        cancelButton = new JButton("Cancel");
        resetButton = new JButton("Reset"); 
        statusLabel = new JLabel("Enter URL and click Download");
        imagePanel = new JPanel();
        progressBar = new JProgressBar();

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                if (!url.isEmpty()) {
                    startImageDownload(url);
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseDownloads();
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resumeDownloads();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelDownloads();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetControls();
            }
        });

        add(createInputPanel(), BorderLayout.NORTH);
        add(createImagePanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel urlLabel = new JLabel("URL:");
        urlField.setPreferredSize(new Dimension(400, 25));

        inputPanel.add(urlLabel);
        inputPanel.add(urlField);
        inputPanel.add(downloadButton);

        return inputPanel;
    }

    private JPanel createImagePanel() {
        imagePanel.setLayout(new BorderLayout());
        return imagePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        controlPanel.add(pauseButton);
        controlPanel.add(resumeButton);
        controlPanel.add(cancelButton);
        controlPanel.add(resetButton); 
        controlPanel.add(progressBar);
        controlPanel.add(statusLabel);

        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);

        return controlPanel;
    }

    private void startImageDownload(String imageUrl) {
        try {
            if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
                throw new MalformedURLException("Invalid URL: Missing protocol");
            }

            threadPool = Executors.newFixedThreadPool(5);
            downloadTasks = new CopyOnWriteArrayList<>();

            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    lock.lock();
                    try {
                        URL url = new URL(imageUrl);
                        BufferedImage image = ImageIO.read(url);
                        displayImage(image);
                        statusLabel.setText("Image downloaded successfully.");
                    } catch (IOException e) {
                        handleImageDownloadError(e);
                    } finally {
                        lock.unlock();
                    }
                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    for (Integer chunk : chunks) {
                        progressBar.setValue(chunk);
                    }
                }

                @Override
                protected void done() {
                    resetControls();
                }
            };

            downloadTasks.add(threadPool.submit(worker));
            updateControlsOnDownloadStart();
        } catch (MalformedURLException e) {
            handleInvalidURLError(e);
        }
    }

    private void displayImage(BufferedImage image) {
        imagePanel.removeAll();
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    private void pauseDownloads() {
        lock.lock();
        try {
            for (Future<?> task : downloadTasks) {
                if (!task.isDone() && !task.isCancelled()) {
                    task.cancel(true);
                }
            }
            updateControlsOnDownloadPause();
        } finally {
            lock.unlock();
        }
    }

    private void resumeDownloads() {
        startImageDownload(urlField.getText());
    }

    private void cancelDownloads() {
        lock.lock();
        try {
            for (Future<?> task : downloadTasks) {
                if (!task.isDone() && !task.isCancelled()) {
                    task.cancel(true);
                }
            }
            threadPool.shutdownNow();
            resetControls();
        } finally {
            lock.unlock();
        }
    }

    private void updateControlsOnDownloadStart() {
        downloadButton.setEnabled(false);
        pauseButton.setEnabled(true);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(true);
    }

    private void updateControlsOnDownloadPause() {
        downloadButton.setEnabled(false);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(true);
        cancelButton.setEnabled(true);
    }

    private void resetControls() {
        statusLabel.setText("Enter URL and click Download");
        urlField.setText("");  
        imagePanel.removeAll(); 
        progressBar.setValue(0);
        threadPool.shutdown();

        downloadButton.setEnabled(true);
        pauseButton.setEnabled(false);
        resumeButton.setEnabled(false);
        cancelButton.setEnabled(false);
    }

    private void handleInvalidURLError(MalformedURLException e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("Invalid URL: Please enter a valid URL with a protocol (e.g., http:// or https://)");
            }
        });
        e.printStackTrace();
    }

    private void handleImageDownloadError(IOException e) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                statusLabel.setText("Failed to download image: " + e.getMessage());
            }
        });
        e.printStackTrace();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Imagedownloader().setVisible(true);
            }
        });
    }
}
