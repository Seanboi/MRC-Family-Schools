import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class RegisterGUI extends JFrame {
    private JTable attendanceTable;
    private DefaultTableModel tableModel;
    private JButton saveAttendanceButton, addDateButton, removeDateButton, editDateButton;
    private String classID;

    public RegisterGUI(String classID) {
        this.classID = classID;

        setTitle(classID + " Register");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(classID + " REGISTER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(0, 70, 140));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel();
        attendanceTable = new JTable(tableModel);
        loadAttendanceData();

        attendanceTable.setRowHeight(25);
        attendanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        attendanceTable.getTableHeader().setBackground(new Color(0, 120, 215));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 12));
        attendanceTable.setGridColor(Color.LIGHT_GRAY);
        attendanceTable.setShowGrid(true);

        add(new JScrollPane(attendanceTable), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        addDateButton = createButton("Add Date");
        removeDateButton = createButton("Remove Date");
        editDateButton = createButton("Edit Date");
        saveAttendanceButton = createButton("Save Attendance");
        buttonPanel.add(addDateButton);
        buttonPanel.add(removeDateButton);
        buttonPanel.add(editDateButton);
        buttonPanel.add(saveAttendanceButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setupAddDateButton();
        setupRemoveDateButton();
        setupEditDateButton();
        setupSaveAttendanceButton();
        setupAttendanceDropdownEditor();

        setVisible(true);
    }

    private JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(150, 40));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 120, 215));
        button.setFocusPainted(false);
        return button;
    }

    private void loadAttendanceData() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        File attendanceFile = new File(classID + "_attendance.txt");

        if (attendanceFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(attendanceFile))) {
                String headerLine = reader.readLine();
                ArrayList<String> dateColumns = new ArrayList<>();

                if (headerLine != null) {
                    String[] headers = headerLine.split(",");

                    for (int i = 2; i < headers.length; i++) {
                        dateColumns.add(headers[i]);
                    }

                    dateColumns.sort((d1, d2) -> {
                        try {
                            return java.time.LocalDate.parse(d2).compareTo(java.time.LocalDate.parse(d1));
                        } catch (Exception e) {
                            return 0;
                        }
                    });

                    tableModel.addColumn(headers[0]);
                    tableModel.addColumn(headers[1]);
                    for (String date : dateColumns) {
                        tableModel.addColumn(date);
                    }
                }

                ArrayList<String[]> rows = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    rows.add(line.split(","));
                }

                rows.sort((a, b) -> {
                    String lastNameA = a[1].substring(a[1].lastIndexOf(" ") + 1);
                    String lastNameB = b[1].substring(b[1].lastIndexOf(" ") + 1);
                    return lastNameA.compareToIgnoreCase(lastNameB);
                });

                for (String[] row : rows) {
                    ArrayList<String> sortedRow = new ArrayList<>();
                    sortedRow.add(row[0]);
                    sortedRow.add(row[1]);

                    for (String date : dateColumns) {
                        int colIndex = findHeaderIndex(headerLine.split(","), date);
                        sortedRow.add(colIndex < row.length ? row[colIndex] : "");
                    }

                    tableModel.addRow(sortedRow.toArray());
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error loading attendance data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Attendance file not found.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private int findHeaderIndex(String[] headers, String headerToFind) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(headerToFind)) {
                return i;
            }
        }
        return -1;
    }

    private void setupAttendanceDropdownEditor() {
        String[] attendanceOptions = {"Present", "Absent", "Late"};

        for (int i = 2; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);

            if (isDateColumn(columnName)) {
                attendanceTable.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JComboBox<>(attendanceOptions)));
            } else {
                attendanceTable.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JTextField()));
            }
        }
    }

    private boolean isDateColumn(String columnName) {
        try {
            java.time.LocalDate.parse(columnName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void setupAddDateButton() {
        addDateButton.addActionListener(e -> {
            JPanel datePanel = new JPanel(new FlowLayout());
            JComboBox<String> dayComboBox = new JComboBox<>(generateNumbers(1, 31));
            JComboBox<String> monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"});
            JComboBox<String> yearComboBox = new JComboBox<>(generateNumbers(2020, 2030));

            datePanel.add(new JLabel("Day:"));
            datePanel.add(dayComboBox);
            datePanel.add(new JLabel("Month:"));
            datePanel.add(monthComboBox);
            datePanel.add(new JLabel("Year:"));
            datePanel.add(yearComboBox);

            int result = JOptionPane.showConfirmDialog(this, datePanel, "Select Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String selectedDay = (String) dayComboBox.getSelectedItem();
                String selectedMonth = String.valueOf(monthComboBox.getSelectedIndex() + 1);
                String selectedYear = (String) yearComboBox.getSelectedItem();

                String newDate = String.format("%04d-%02d-%02d", Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth), Integer.parseInt(selectedDay));

                if (!isDateColumnExists(newDate)) {
                    tableModel.addColumn(newDate);
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        tableModel.setValueAt("Absent", i, tableModel.getColumnCount() - 1);
                    }
                    sortColumnsChronologically();
                    setupAttendanceDropdownEditor();
                } else {
                    JOptionPane.showMessageDialog(this, "Date already exists in the attendance record.", "Duplicate Date", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private void setupRemoveDateButton() {
        removeDateButton.addActionListener(e -> {
            String[] dates = new String[tableModel.getColumnCount() - 2];
            for (int i = 2; i < tableModel.getColumnCount(); i++) {
                dates[i - 2] = tableModel.getColumnName(i);
            }

            String selectedDate = (String) JOptionPane.showInputDialog(this, "Select Date to Remove:", "Remove Date",
                    JOptionPane.PLAIN_MESSAGE, null, dates, dates[0]);

            if (selectedDate != null) {
                removeDateFromFile(selectedDate);
                loadAttendanceData();
            }
        });
    }

    private void removeDateFromFile(String date) {
        File attendanceFile = new File(classID + "_attendance.txt");
        if (attendanceFile.exists()) {
            try {
                // Read the current content of the file
                BufferedReader reader = new BufferedReader(new FileReader(attendanceFile));
                StringBuilder fileContent = new StringBuilder();
                String line;
                boolean isFirstLine = true;
    
                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(",");
                    if (isFirstLine) {
                        // For the first line, remove the date column
                        ArrayList<String> headers = new ArrayList<>();
                        for (String column : columns) {
                            if (!column.equals(date)) {
                                headers.add(column);
                            }
                        }
                        fileContent.append(String.join(",", headers)).append("\n");
                        isFirstLine = false;
                    } else {
                        // For the rest of the lines, remove the corresponding column for the date
                        ArrayList<String> row = new ArrayList<>();
                        for (int i = 0; i < columns.length; i++) {
                            if (i < 2 || !columns[i].equals(date)) {  // Keep "Student ID" and "Student Name"
                                row.add(columns[i]);
                            }
                        }
                        fileContent.append(String.join(",", row)).append("\n");
                    }
                }
                reader.close();
    
                // Write the updated content back to the file
                BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile));
                writer.write(fileContent.toString());
                writer.close();
    
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error removing the date from the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Attendance file not found.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    private void setupEditDateButton() {
        editDateButton.addActionListener(e -> {
            String[] dates = new String[tableModel.getColumnCount() - 2];
            for (int i = 2; i < tableModel.getColumnCount(); i++) {
                dates[i - 2] = tableModel.getColumnName(i);
            }

            String selectedDate = (String) JOptionPane.showInputDialog(this, "Select Date to Edit:", "Edit Date",
                    JOptionPane.PLAIN_MESSAGE, null, dates, dates[0]);

            if (selectedDate != null) {
                JPanel datePanel = new JPanel(new FlowLayout());
                JComboBox<String> dayComboBox = new JComboBox<>(generateNumbers(1, 31));
                JComboBox<String> monthComboBox = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June",
                        "July", "August", "September", "October", "November", "December"});
                JComboBox<String> yearComboBox = new JComboBox<>(generateNumbers(2020, 2030));

                String[] dateParts = selectedDate.split("-");
                if (dateParts.length == 3) {
                    yearComboBox.setSelectedItem(dateParts[0]);
                    monthComboBox.setSelectedIndex(Integer.parseInt(dateParts[1]) - 1);
                    dayComboBox.setSelectedItem(dateParts[2]);
                }

                datePanel.add(new JLabel("Day:"));
                datePanel.add(dayComboBox);
                datePanel.add(new JLabel("Month:"));
                datePanel.add(monthComboBox);
                datePanel.add(new JLabel("Year:"));
                datePanel.add(yearComboBox);

                int result = JOptionPane.showConfirmDialog(this, datePanel, "Edit Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    String newDay = (String) dayComboBox.getSelectedItem();
                    String newMonth = String.valueOf(monthComboBox.getSelectedIndex() + 1);
                    String newYear = (String) yearComboBox.getSelectedItem();

                    String newDate = String.format("%04d-%02d-%02d", Integer.parseInt(newYear), Integer.parseInt(newMonth), Integer.parseInt(newDay));

                    if (!isDateColumnExists(newDate)) {
                        updateDateInFile(selectedDate, newDate);
                        loadAttendanceData();
                    }
                }
            }
        });
    }

    private void updateDateInFile(String oldDate, String newDate) {
        File attendanceFile = new File(classID + "_attendance.txt");
        if (attendanceFile.exists()) {
            try {
                // Read the current content of the file
                BufferedReader reader = new BufferedReader(new FileReader(attendanceFile));
                StringBuilder fileContent = new StringBuilder();
                String line;
                boolean isFirstLine = true;
    
                while ((line = reader.readLine()) != null) {
                    String[] columns = line.split(",");
                    if (isFirstLine) {
                        // For the first line, update the date header
                        ArrayList<String> headers = new ArrayList<>();
                        for (String column : columns) {
                            if (column.equals(oldDate)) {
                                headers.add(newDate); // Replace the old date with the new one
                            } else {
                                headers.add(column);
                            }
                        }
                        fileContent.append(String.join(",", headers)).append("\n");
                        isFirstLine = false;
                    } else {
                        // For the rest of the lines, update the corresponding date column
                        ArrayList<String> row = new ArrayList<>();
                        for (int i = 0; i < columns.length; i++) {
                            if (columns[i].equals(oldDate)) {
                                row.add(newDate);  // Replace the old date with the new one
                            } else {
                                row.add(columns[i]);
                            }
                        }
                        fileContent.append(String.join(",", row)).append("\n");
                    }
                }
                reader.close();
    
                // Write the updated content back to the file
                BufferedWriter writer = new BufferedWriter(new FileWriter(attendanceFile));
                writer.write(fileContent.toString());
                writer.close();
    
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error updating the date in the file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Attendance file not found.", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    



    private void setupSaveAttendanceButton() {
        saveAttendanceButton.addActionListener(e -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(classID + "_attendance.txt"))) {
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    writer.write(tableModel.getColumnName(i));
                    if (i < tableModel.getColumnCount() - 1) writer.write(",");
                }
                writer.newLine();

                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    for (int col = 0; col < tableModel.getColumnCount(); col++) {
                        writer.write(String.valueOf(tableModel.getValueAt(row, col)));
                        if (col < tableModel.getColumnCount() - 1) writer.write(",");
                    }
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(this, "Attendance saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(this, "Error saving attendance data: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private boolean isDateColumnExists(String date) {
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            if (tableModel.getColumnName(i).equals(date)) {
                return true;
            }
        }
        return false;
    }

    private void sortColumnsChronologically() {
        ArrayList<String> columnNames = new ArrayList<>();
        ArrayList<ArrayList<String>> rows = new ArrayList<>();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            ArrayList<String> rowData = new ArrayList<>();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                if (i == 0) columnNames.add(tableModel.getColumnName(j));
                rowData.add((String) tableModel.getValueAt(i, j));
            }
            rows.add(rowData);
        }

        ArrayList<String> fixedColumns = new ArrayList<>(columnNames.subList(0, 2));
        ArrayList<String> dateColumns = new ArrayList<>(columnNames.subList(2, columnNames.size()));

        dateColumns.sort((a, b) -> {
            try {
                return java.time.LocalDate.parse(a).compareTo(java.time.LocalDate.parse(b));
            } catch (Exception e) {
                return 0;
            }
        });

        ArrayList<String> sortedColumns = new ArrayList<>(fixedColumns);
        sortedColumns.addAll(dateColumns);

        ArrayList<ArrayList<String>> sortedRows = new ArrayList<>();
        for (ArrayList<String> row : rows) {
            ArrayList<String> sortedRow = new ArrayList<>();
            sortedRow.addAll(row.subList(0, 2));
            for (String date : dateColumns) {
                int index = columnNames.indexOf(date);
                sortedRow.add(row.get(index));
            }
            sortedRows.add(sortedRow);
        }

        tableModel.setColumnCount(0);
        for (String column : sortedColumns) {
            tableModel.addColumn(column);
        }
        tableModel.setRowCount(0);
        for (ArrayList<String> row : sortedRows) {
            tableModel.addRow(row.toArray());
        }
    }

    private String[] generateNumbers(int start, int end) {
        String[] numbers = new String[end - start + 1];
        for (int i = start; i <= end; i++) {
            numbers[i - start] = String.valueOf(i);
        }
        return numbers;
    }
}
