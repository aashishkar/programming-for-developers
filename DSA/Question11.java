import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Question11 extends JFrame {
    private JLabel statusLabel;
    private JTextField urlField;
    private JButton downloadButton;
    private ExecutorService executor;

    public Question11() {
        setTitle("Image Downloader");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);
        setLocationRelativeTo(null);

        statusLabel = new JLabel("Enter URL of the image to download:");
        urlField = new JTextField(30);
        downloadButton = new JButton("Download");

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(statusLabel);
        panel.add(urlField);
        panel.add(downloadButton);

        add(panel);

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlField.getText();
                if (!url.isEmpty()) {
                    downloadImageAsync(url);
                } else {
                    JOptionPane.showMessageDialog(Question11.this, "Please enter a valid URL");
                }
            }
        });

        executor = Executors.newFixedThreadPool(5); // Adjust the number of threads as needed
    }

    private void downloadImageAsync(String imageUrl) {
        executor.execute(() -> {
            try {
                URL url = new URL(imageUrl);
                InputStream in = url.openStream();
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                FileOutputStream out = new FileOutputStream(fileName);

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                in.close();
                out.close();

                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Question11.this, "Image downloaded successfully!");
                });
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(Question11.this, "Error downloading image: " + e.getMessage());
                });
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Question11 downloader = new Question11();
            downloader.setVisible(true);
        });
    }
}
