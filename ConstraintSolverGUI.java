import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class ConstraintSolverGUI extends JFrame {
    private List<String> variables;
    private List<String> domainValues;
    private List<String[]> constraints;
    private boolean solutionFound;
    private JTextArea solutionTextArea;

    public ConstraintSolverGUI() {
        variables = new ArrayList<>();
        domainValues = new ArrayList<>();
        constraints = new ArrayList<>();

        setTitle("Constraint Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));

        JButton variablesButton = new JButton("Enter Courses");
        variablesButton.addActionListener(e -> setVariables());
        buttonPanel.add(variablesButton);

        JButton domainValuesButton = new JButton("Set Days");
        domainValuesButton.addActionListener(e -> setDomainValues());
        buttonPanel.add(domainValuesButton);

        JButton constraintsButton = new JButton("Set Constraints");
        constraintsButton.addActionListener(e -> setConstraints());
        buttonPanel.add(constraintsButton);

        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> solveConstraints());
        buttonPanel.add(solveButton);

        solutionTextArea = new JTextArea();
        solutionTextArea.setEditable(false);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(solutionTextArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void setVariables() {
        String input = JOptionPane.showInputDialog(this, "Enter variables separated by comma (e.g., A, B, C):");
        if (input != null) {
            String[] vars = input.trim().split("\\s*,\\s*");
            for (String var : vars) {
                variables.add(var);
            }
        }
    }

    private void setDomainValues() {
        String input = JOptionPane.showInputDialog(this, "Enter domain values separated by comma (e.g., Monday, Tuesday, Wednesday):");
        if (input != null) {
            String[] values = input.trim().split("\\s*,\\s*");
            for (String value : values) {
                domainValues.add(value);
            }
        }
    }

    private void setConstraints() {
        constraints.clear();
        while (true) {
            String input = JOptionPane.showInputDialog(this, "Enter constraint (e.g., A, B): (Leave blank to finish)");
            if (input == null || input.isEmpty()) {
                break;
            }
            String[] constraint = input.trim().split("\\s*,\\s*");
            constraints.add(constraint);
        }
    }

    private void solveConstraints() {
        solutionFound = false;
        Map<String, String> assignment = new HashMap<>();
        backtrack(assignment);
        if (!solutionFound) {
            solutionTextArea.setText("No solution found!");
        }
    }

    private void backtrack(Map<String, String> assignment) {
        if (solutionFound) {
            return;
        }
        
        if (assignment.size() == variables.size()) {
            solutionTextArea.setText("Solution found: " + assignment);
            solutionFound = true;
            return;
        }
        
        String unassignedVar = selectUnassignedVariable(assignment);
        
        for (String value : domainValues) {
            Map<String, String> newAssignment = new HashMap<>(assignment);
            newAssignment.put(unassignedVar, value);
            if (consistent(newAssignment)) {
                backtrack(newAssignment);
            }
            if (solutionFound) {
                return;
            }
        }
    }

    private String selectUnassignedVariable(Map<String, String> assignment) {
        for (String variable : variables) {
            if (!assignment.containsKey(variable)) {
                return variable;
            }
        }
        return null;
    }

    private boolean consistent(Map<String, String> assignment) {
        Map<String, Integer> valueCount = new HashMap<>();
        for (String value : domainValues) {
            valueCount.put(value, 0);
        }

        for (String[] constraint : constraints) {
            String var1 = constraint[0];
            String var2 = constraint[1];
            if (assignment.containsKey(var1) && assignment.containsKey(var2)) {
                if (assignment.get(var1).equals(assignment.get(var2))) {
                    return false;
                }
               
            }
        }
        Iterator hmIterator=assignment.entrySet().iterator();
        while(hmIterator.hasNext())
        {
            Map.Entry mapEntry=(Map.Entry)hmIterator.next();
            valueCount.put((String)assignment.get(mapEntry.getKey()), valueCount.get(assignment.get(mapEntry.getKey())) + 1);
;
        }

        for (int count : valueCount.values()) {
            if (count > 2) {
                return false;
            }
        }
        
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConstraintSolverGUI::new);
    }
}
