/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import DataStructure.Questions;
import DataStructure.test;
import GUI.FRQPanel;
import GUI.MCPanel;
import GUI.SaveTestPanel;
import GUI.addTestPanel;
import GUI.openTestPanel;
import GUI.openPicturePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import javax.swing.JPanel;


/**
 *
 * @author zepingluo
 */
public class Mainframe extends javax.swing.JFrame {

    

    public ArrayList<test> testList;
   
    public test currentTest;
    
    //for currentTest, the testFilePath would be in
    //" folderPath/testName " format
    //representing a test text file.
    
    public String testFilePath;
    
    //for current folder, the folderPath would be in 
    //" DefaultDirectory/testName " format
    //representing a directory contatining test text file and 
    //pictures.
    
    public String folderPath;
    //this is a list
    
    
    public ArrayList<Questions> questionList;
    //this is for the correct display of question in the order
    public int numQuestion;
    
    public static final int EASY = 0;
    public static final int MEDIUM = 1;
    public static final int HARD = 2;
    public static final int UNKNOWN = -1;
    public static final int MULTIPLECHOICE = 0;
    public static final int FREERESPONSE = 1;
    public static String DEFAULTDIRECTORY ;
    //public static final String DEFAULTDIRECTORY = "/Users/zepingluo/documents/testFiles";
    /**
     * Creates new form Mainframe
     */
    public Mainframe() {
        initComponents();
        
        testList = new ArrayList<>();
        iniImport();
        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        save_and_new_test_Button.setEnabled(false);
        
        setQuestionPanel(new openTestPanel(this));
        testNameField.setVisible(false);
        this.getContentPane().setBackground(new Color(204,204,255));
        
    }
    
    //this methods first ask for the default directory from which the program imports test,
    //to which the program adds new tests
    public void iniImport()
    {
        //import tests in testFiles
        String input = JOptionPane.showInputDialog(this, "enter the default", "info", QUESTION_MESSAGE);
        if(input.equals("danny"))
        {
              DEFAULTDIRECTORY="/Users/zepingluo/documents/testFiles";
        }else
        {
            DEFAULTDIRECTORY=input;
        }
      
        
        File directory = new File(DEFAULTDIRECTORY);
        File[] contents = directory.listFiles();
        for(int i=1;i<contents.length;i++)
        {
            importTest(contents[i].getAbsolutePath());
            
        }
        for(int i=0;i<testList.size();i++)
        {
            System.out.println(testList.get(i).getTestName());
        }
    }
    public void loadTest(test test)
    {
        currentTest= test;
        questionList=currentTest.getTestQuestionList();
        numQuestion=-1;
        addButton.setEnabled(true);
        save_and_new_test_Button.setEnabled(true);
        QuestionPanel.setLayout(new BorderLayout());
        QuestionPanel.updateUI();
        QuestionPanel.removeAll();
        testNameField.setText(currentTest.getTestName());
        testNameField.setVisible(true);
          drawList();
         
        
        //filePath?
        
    }
    public void setQuestionPanel(JPanel panel) {
        QuestionPanel.setLayout(new BorderLayout());
        QuestionPanel.updateUI();
        QuestionPanel.removeAll();

        QuestionPanel.add(panel, BorderLayout.CENTER);
    }

    public void loadQuestion() {
        //?
        //removeButton.setEnabled(true);
        addButton.setEnabled(true);
        save_and_new_test_Button.setEnabled(true);
        //?
        int type = questionList.get(numQuestion).getType();
        if (type == MULTIPLECHOICE) {
            setQuestionPanel(new MCPanel(this));
        } else {
            setQuestionPanel(new FRQPanel(this));
        }


    }

    public void addQuestion() {
        numQuestion++;
        String[] options = {"Multiple choice", "Free Response"};

        int choice = JOptionPane.showOptionDialog(this, //Component parentComponent
                "MC or FRQ?", //Object message,
                "Choose an option", //String title
                JOptionPane.YES_NO_OPTION, //int optionType
                JOptionPane.INFORMATION_MESSAGE, //int messageType
                null, //Icon icon,
                options, //Object[] options,
                "Multiple choice");//Object initialValue 
        if (choice == 0) {
            //Metric was chosen
            
            questionList.add(new Questions(MULTIPLECHOICE));
            setQuestionPanel(new MCPanel(this));
            System.out.println(questionList.size());

        } else {
            //Imperial was chosen
            questionList.add(new Questions(FREERESPONSE));
            setQuestionPanel(new FRQPanel(this));
            
            System.out.println(questionList.size());

        }
      
        loadQuestion();
        //numQuestion++;
        drawList();
    }
    
    public void drawList() {
        DefaultListModel dlm = new DefaultListModel();
        for (int i = 0; i < questionList.size(); i++) {
            dlm.addElement("question " + (i+1));
        }
        QuestionList.setModel(dlm);
    }

    public void importTest(String folderPath)
    {
       //removeButton.setEnabled(true);
       addButton.setEnabled(true);
       save_and_new_test_Button.setEnabled(true);
        ArrayList<Questions> testQuestionList = new ArrayList<Questions>();
       
        String[] filePath=folderPath.split("/");
        testFilePath=folderPath+"/"+filePath[filePath.length-1];
                try {
            // Load file and read info to RAM from file
            BufferedReader loadFile = new BufferedReader(new FileReader(testFilePath));
            String testName=loadFile.readLine();
            String testWriter=loadFile.readLine();
            int time=Integer.parseInt(loadFile.readLine());
            currentTest=new test(testName,testWriter);
            
            currentTest.setTime(time);
            currentTest.setList(testQuestionList);
            
            questionList=testQuestionList;
            numQuestion=-1;
           
            String input;
            
            
            // Continue to read in from text file 2 lines for each athlete
            // while there are still line to be read in
            // First line is their personal info
            // Second line is their goal data
            while ((input = loadFile.readLine()) != null) {
                
                
                String type = input;

                if(type.equalsIgnoreCase("MC"))
                {
                    String stem = loadFile.readLine();
                    
                    input = loadFile.readLine();
                    String[] answerChoices = input.split(",");
                    String correctAnswer=loadFile.readLine();
                   
                    int difficulty = Integer.parseInt(loadFile.readLine());
                    
                    testQuestionList.add(new Questions(MULTIPLECHOICE, difficulty, stem, answerChoices,correctAnswer));
              
                }
                else if(type.equalsIgnoreCase("FRQ"))
                {
                    String stem = loadFile.readLine();
                    String correctAnswer = loadFile.readLine();
                    
                    int difficulty = Integer.parseInt(loadFile.readLine());
                    testQuestionList.add(new Questions(FREERESPONSE,stem,correctAnswer,difficulty) );
                    
                }

            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error trying to "
                    + "load file: " + ex,
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        for (int i = 1; i <= questionList.size(); i++) {
            File file = new File(folderPath + "/" + "q" + i + ".jpg");
            if (file.exists()) {

                questionList.get(i - 1).setImageFile(file.getAbsolutePath());
            }
        }
        for(int i = 0; i < testList.size(); i++)
        {
            if(testList.get(i).getTestName().equals(currentTest.getTestName()))
            {
                testList.remove(i);
            }
        }
        testList.add(currentTest);
        //loadTest(currentTest);
          

    }
    public void save() {
        try {
            PrintWriter writer = new PrintWriter(new File(testFilePath));
            System.out.println("save() and the file path"+testFilePath);
            writer.println(currentTest.getTestName());
            writer.println(currentTest.getTestWriter());
            writer.println(currentTest.getTime());
            for(int n=0;n< questionList.size(); n++)
            {
                Questions cur = questionList.get(n);
                
                if(cur.getType()==MULTIPLECHOICE)
                {
                    writer.println("MC");
                    writer.println(cur.getStem());
                    
                    for(int i=0;i<3;i++)
                    {
                        writer.print(cur.getChoices()[i]);
                        writer.print(",");
                    }
                    writer.println(cur.getChoices()[3]);
                    writer.println(cur.getCorrectAnswer());
                    writer.println(""+cur.getDifficulty());
                    //writer.println(cur.getImageFile());
                }
                if(cur.getType()==FREERESPONSE)
                {
                    writer.println("FRQ");
                    writer.println(cur.getStem());
                    writer.println(cur.getCorrectAnswer());
                    writer.println(""+cur.getDifficulty());
                   // writer.println(cur.getImageFile());
                }
                
            }
            writer.close();
                    
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mainframe.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
    }

   

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        QuestionList = new javax.swing.JList<>();
        QuestionPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        save_and_new_test_Button = new javax.swing.JButton();
        testNameField = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 255));
        setPreferredSize(new java.awt.Dimension(1050, 601));
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

        QuestionList.setFont(new java.awt.Font("Times", 1, 14)); // NOI18N
        QuestionList.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                QuestionListMouseDragged(evt);
            }
        });
        QuestionList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                QuestionListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(QuestionList);

        QuestionPanel.setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout QuestionPanelLayout = new javax.swing.GroupLayout(QuestionPanel);
        QuestionPanel.setLayout(QuestionPanelLayout);
        QuestionPanelLayout.setHorizontalGroup(
            QuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 652, Short.MAX_VALUE)
        );
        QuestionPanelLayout.setVerticalGroup(
            QuestionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
        );

        addButton.setFont(new java.awt.Font("Times", 1, 18)); // NOI18N
        addButton.setForeground(new java.awt.Color(51, 51, 255));
        addButton.setText("Add New Question");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setFont(new java.awt.Font("Times", 1, 18)); // NOI18N
        removeButton.setForeground(new java.awt.Color(255, 51, 51));
        removeButton.setText("Remove Question");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Times", 1, 18)); // NOI18N
        jLabel1.setText("Test Name:");

        save_and_new_test_Button.setFont(jLabel1.getFont());
        save_and_new_test_Button.setText("Save & Back to Menu");
        save_and_new_test_Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                save_and_new_test_ButtonActionPerformed(evt);
            }
        });

        testNameField.setFont(jLabel1.getFont());

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
                        .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(addButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addComponent(QuestionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(layout.createSequentialGroup()
                .addGap(114, 114, 114)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(testNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(save_and_new_test_Button)
                .addGap(145, 145, 145))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(save_and_new_test_Button)
                    .addComponent(testNameField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(removeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(QuestionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        // TODO add your handling code here:
        
        if(testList.size() != 0)
        {
          addButton.setEnabled(true);
            addQuestion();
        }
        

    }//GEN-LAST:event_addButtonActionPerformed

    private void QuestionListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuestionListMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() > 1) {
            removeButton.setEnabled(true);
            int questionIndex = QuestionList.getSelectedIndex();
            numQuestion = questionIndex;
            loadQuestion();
            
            System.out.println(numQuestion);
        }
    }//GEN-LAST:event_QuestionListMouseClicked

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        // TODO add your handling code here:
        removeButton.setEnabled(false);
        File f = new File(folderPath+"/q"+(numQuestion+1)+".jpg");
        f.delete();
        questionList.remove(numQuestion);
        if(numQuestion>0)
        //if(questionIndex>0)
        {
            numQuestion--;
            drawList();
            loadQuestion();
        }
        else
        {
            QuestionPanel.updateUI();
            QuestionPanel.removeAll();
            drawList();
        }
        
    }//GEN-LAST:event_removeButtonActionPerformed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_formMouseDragged

    private void QuestionListMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_QuestionListMouseDragged
        // TODO add your handling code here:
    }//GEN-LAST:event_QuestionListMouseDragged

    public void clearQuestionList()
    {
        DefaultListModel dlm = new DefaultListModel();
        for (int i = 0; i < 0; i++) {
            dlm.addElement("");
        }
        QuestionList.setModel(dlm);
    }
    private void save_and_new_test_ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_save_and_new_test_ButtonActionPerformed
        // TODO add your handling code here:
        
        
        removeButton.setEnabled(false);
        addButton.setEnabled(false);
        save_and_new_test_Button.setEnabled(false);
        JDialog dialog = new JDialog(this, true);
        dialog.add(new SaveTestPanel(this));
        dialog.pack();
        dialog.setVisible(true);
        
        
        testNameField.setText("");
        setQuestionPanel(new openTestPanel(this));
        clearQuestionList();
        
    }//GEN-LAST:event_save_and_new_test_ButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Mainframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Mainframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Mainframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Mainframe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Mainframe().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> QuestionList;
    private javax.swing.JPanel QuestionPanel;
    private javax.swing.JButton addButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton save_and_new_test_Button;
    private javax.swing.JLabel testNameField;
    // End of variables declaration//GEN-END:variables
}
