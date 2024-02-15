import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class SocialMediaApp extends JFrame {
    private JTextField usernameField;
    private JTextArea postContentArea;
    private JButton postButton;
    private JTextArea recommendationArea;

    private SocialNetwork socialNetwork;
    private RecommendationSystem recommendationSystem;
    private User currentUser;

    public SocialMediaApp() {
        setTitle("Social Media App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel postPanel = new JPanel(new BorderLayout());
        postContentArea = new JTextArea(5, 30);
        JScrollPane postScrollPane = new JScrollPane(postContentArea);
        postButton = new JButton("Post");
        postPanel.add(postScrollPane, BorderLayout.CENTER);
        postPanel.add(postButton, BorderLayout.SOUTH);

        JPanel profilePanel = new JPanel(new BorderLayout());
        usernameField = new JTextField(20);
        JButton loginButton = new JButton("Login");
        profilePanel.add(new JLabel("Username: "), BorderLayout.WEST);
        profilePanel.add(usernameField, BorderLayout.CENTER);
        profilePanel.add(loginButton, BorderLayout.EAST);

        recommendationArea = new JTextArea(10, 30);
        recommendationArea.setEditable(false);
        JScrollPane recommendationScrollPane = new JScrollPane(recommendationArea);

        mainPanel.add(profilePanel, BorderLayout.NORTH);
        mainPanel.add(postPanel, BorderLayout.CENTER);
        mainPanel.add(recommendationScrollPane, BorderLayout.SOUTH);

        add(mainPanel);

        socialNetwork = new SocialNetwork();
        recommendationSystem = new RecommendationSystem(socialNetwork);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                if (!username.isEmpty()) {
                    currentUser = socialNetwork.getUser(username);
                    if (currentUser == null) {
                        currentUser = new User(username);
                        socialNetwork.addUser(currentUser);
                    }
                    updateRecommendations();
                } else {
                    JOptionPane.showMessageDialog(SocialMediaApp.this, "Please enter a username.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        postButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = postContentArea.getText();
                if (!content.isEmpty() && currentUser != null) {
                    Post post = new Post(currentUser, content);
                    currentUser.createPost(post);
                    updateRecommendations();
                    postContentArea.setText("");
                }
            }
        });
    }

    private void updateRecommendations() {
        if (currentUser != null) {
            List<Post> recommendations = recommendationSystem.recommendPosts(currentUser);
            recommendationArea.setText("Recommendations for " + currentUser.getUsername() + ":\n");
            for (Post post : recommendations) {
                recommendationArea.append(post.getContent() + "\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SocialMediaApp().setVisible(true);
            }
        });
    }
}

class SocialNetwork {
    private Graph<User, DefaultEdge> network;

    public SocialNetwork() {
        network = new DefaultUndirectedGraph<>(DefaultEdge.class);
    }

    public void addUser(User user) {
        network.addVertex(user);
    }

    public void addConnection(User user1, User user2) {
        network.addEdge(user1, user2);
    }

    public User getUser(String username) {
        for (User user : network.vertexSet()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}

class User {
    private String username;
    private Set<User> friends;
    private List<Post> posts;

    public User(String username) {
        this.username = username;
        friends = new HashSet<>();
        posts = new ArrayList<>();
    }

    public void addFriend(User friend) {
        friends.add(friend);
    }

    public void createPost(Post post) {
        posts.add(post);
    }

    public String getUsername() {
        return username;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public List<Post> getPosts() {
        return posts;
    }
}

class Post {
    private User author;
    private String content;

    public Post(User author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

class RecommendationSystem {
    private SocialNetwork socialNetwork;

    public RecommendationSystem(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public List<Post> recommendPosts(User user) {
        // Dummy recommendation logic - recommend posts of user's friends
        List<Post> recommendations = new ArrayList<>();
        for (User friend : user.getFriends()) {
            recommendations.addAll(friend.getPosts());
        }
        return recommendations;
    }
}
