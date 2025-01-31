import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

/**
 * LoginPage class provides a graphical user interface for user authentication
 * in a Point of Sale (POS) system.
 * It features a text field for entering an employee ID and a button to initiate
 * the login process. Upon successful
 * authentication, the user is granted access to the POS system's menu page;
 * otherwise, an error message is displayed.
 * The class requires a connection to a database to verify employee credentials
 * against stored records.
 * 
 * This class is part of a larger POS application designed to streamline retail
 * operations. It demonstrates basic
 * authentication techniques and UI design using Swing components. The
 * implementation includes handling of
 * action events for login attempts and executing SQL queries against the
 * provided database connection to verify user credentials.
 * 
 * @author Segun Oluwole
 */
public class LoginPage extends JPanel {
    private JPasswordField idField;
    private JButton loginButton;
    private Connection conn;
    private POS pos;

    /**
     * This is the constructor for the LoginPage object. It creates a LoginPage
     * object that can
     * is used to login to the POS system. The conn argument must be an already
     * established
     * SQL database connection, and the pos argument must be an already created POS
     * object.
     * 
     * @param conn, a sql Connection object that represents the connection to the
     *              database
     * @param pos,  the POS object that acts as the main driver for the program
     */
    public LoginPage(Connection conn, POS pos) {
        this.conn = conn;
        this.pos = pos;
        initializeUI();

    }

    /**
     * Creates all the Java Swing components and adds them to the Login Page
     * 
     * @param none
     */
    private void initializeUI() {

        setBackground(Common.MAROON);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 10, 0); // Add spacing below the idField

        // Create a panel to hold the label and password field
        JPanel idPanel = new JPanel();
        idPanel.setOpaque(false);
        idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.Y_AXIS)); // Set layout to vertical

        // Add "ID:" label
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(new Font("Arial", Font.BOLD, 20));
        idLabel.setForeground(Color.WHITE);
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        idLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100)); // Add empty border on the right to left align
                                                                          // with idField
        idPanel.add(idLabel);
        idPanel.add(Box.createVerticalStrut(5)); // Add some vertical space

        // Add idField
        idField = new JPasswordField(20);
        idField.setPreferredSize(new Dimension(200, 40));
        idField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(); // Call the login method when Enter is pressed
            }
        });

        idPanel.add(idField);

        add(idPanel, gbc);
        gbc.gridy++; // Move to the next row
        gbc.insets = new Insets(0, 0, 0, 0); // Reset insets for the login button

        // Create a panel to hold the login button and center it horizontally
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(); // Call the login method when the button is clicked
            }
        });
        buttonPanel.add(loginButton);
        add(buttonPanel, gbc);

    }

    /**
     * Queries the database for all the employees and looks through
     * all the results to chekc if the entered login ID exists. If it does exist,
     * it logs in and brings the user to the menu page. If it doesn't, it gives the
     * user
     * an error message and allows for another ID to be entered.
     * 
     * @param none
     * @throws SQLException, if an invalid employee ID is entered
     */
    private void login() {
        // Perform database check here
        String enteredID = new String(idField.getPassword());
        String sqlStatement = "SELECT * FROM Employees where EmployeeID = '" + enteredID + "';";
        // If the ID is found, proceed to the menu page
        try {
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sqlStatement);
            if (result.next()) {
                // Employee ID found in the database
                // You can perform further actions here
                System.out.println("Employee ID found");
                pos.setEmployeeID(enteredID); // Save the entered ID
                System.out.println("Employee ID = " + pos.getEmployeeID());
                pos.showMenuPage();
            } else {
                // Employee ID not found in the database
                // You can handle this case accordingly
                JOptionPane.showMessageDialog(null, "Invalid Employee ID", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Employee ID not found");
            }
        } catch (SQLException exc) {
            // Handle any potential exceptions
            exc.printStackTrace();
        }
    }
}