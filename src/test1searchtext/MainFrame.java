/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1searchtext;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 *
 * @author Anton
 */

public class MainFrame extends JFrame{   
    
    private JLabel textSearching = new JLabel("Введите текст для поиска:");
    private JLabel options = new JLabel("Параметры");
    private JLabel folderLabel = new JLabel("Выбор каталога для поиска:");
    private JLabel extensionLabel = new JLabel("Расширение фалов в которых будет производится поиск:");
    private JTextArea  entryField = new JTextArea (7, 100);
    private JTextField extensionField = new JTextField(10);
    private JList dir = new JList();
    private JList resulList = new JList();
    private JButton startSearchButton = new JButton("Начать поиск");
    private JTree choiceFolderTree;
    private JDesktopPane myDesktop = new JDesktopPane();    
    private JMenuBar menuBar = new JMenuBar();
    private JMenu startMenu = new JMenu("Начать работу");
    private JMenuItem startMenuItem = new JMenuItem("Начать поиск");
    private JMenuItem openFileMenuItem = new JMenuItem("Открыть файл");
    private JMenuItem exitMenuItem = new JMenuItem("Выход");
    private File discs[] = File.listRoots();
    private String nameOfDir;
    private Vector<String> foundFiles = new Vector<String>();
    
    public MainFrame() {
        super("Search Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 700));
        
        myDesktop.setBackground(Color.lightGray);       
        extensionField.setText("*.log");
        startMenu.add(startMenuItem);
        startMenu.add(openFileMenuItem);
        startMenu.addSeparator();
        startMenu.add(exitMenuItem);
        MyTreeNode root = new MyTreeNode(".", "MyComputer", true);        
        DefaultTreeModel model = new DefaultTreeModel(root);       
        choiceFolderTree = new JTree();
        choiceFolderTree.setMinimumSize(new Dimension(200, 250));        
        JScrollPane sp = new JScrollPane(choiceFolderTree);
        sp.setPreferredSize(new Dimension(300, 250));
        choiceFolderTree.setShowsRootHandles(true);
        choiceFolderTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                TreePath path = event.getPath();                                
                MyTreeNode node = (MyTreeNode) path.getLastPathComponent(); 
                File []file = new File(node.getFullPathName()).listFiles();
                nameOfDir = node.getFullPathName();
                if(path.getLastPathComponent() instanceof MyTreeNode){
                    node.loadChildren(model, file);
                }                
            }
            @Override
            public void treeWillCollapse(TreeExpansionEvent event)
                throws ExpandVetoException {}
        });       
        choiceFolderTree.setModel(model);
        root.loadChildren(model, discs);
        
        startMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame searchIF = new JInternalFrame("Search Option",false, true, true, true);
                searchIF.setLayout(new GridBagLayout());
                searchIF.setSize(500, 500);
                searchIF.add(textSearching, new GridBagConstraints(0, 0, 2, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));
                searchIF.add(entryField, new GridBagConstraints(0, 1, 2, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.CENTER, new Insets(1, 1, 1, 1), 0, 0));
                searchIF.add(options, new GridBagConstraints(0, 2, 2, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.CENTER, new Insets(20, 1, 20, 20), 0, 0));
                searchIF.add(folderLabel, new GridBagConstraints(0, 3, 2, 1, 1, 1,
                        GridBagConstraints.NORTH, GridBagConstraints.NORTH, new Insets(1, 1, 1, 1), 0, 0));
                searchIF.add(sp, new GridBagConstraints(0, 4, 2, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 1, 20), 0, 0));
                searchIF.add(extensionLabel, new GridBagConstraints(0, 5, 1, 1, 1, 1,
                        GridBagConstraints.EAST, GridBagConstraints.EAST, new Insets(1, 20, 1, 1), 0, 0));
                searchIF.add(extensionField, new GridBagConstraints(1, 5, 1, 1, 1, 1,
                        GridBagConstraints.WEST, GridBagConstraints.WEST, new Insets(20, 1, 20, 1), 0, 0));
                searchIF.add(startSearchButton, new GridBagConstraints(0, 6, 2, 1, 1, 1,
                        GridBagConstraints.CENTER, GridBagConstraints.CENTER, new Insets(1, 1, 20, 1), 0, 0));
                searchIF.pack();
                myDesktop.add(searchIF);
                searchIF.show();
            }
        });       
        exitMenuItem.addActionListener((ActionEvent e) -> {
            System.exit(1);
        });        
        menuBar.add(startMenu);
        
        resulList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    String nameFoundFile = (String) resulList.getSelectedValue();
                    Desktop desktop = null;
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();                        
                    }

                    try {
                        desktop.open(new File(nameFoundFile));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
                          
        startSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    JInternalFrame resultIF = new JInternalFrame("Search Results", true, true, true);
                    resultIF.setBounds(20, 20, 500, 500);
                    myDesktop.add(resultIF);
                    File catalog = new File(nameOfDir);
                    String textListener = entryField.getText();
                    String extension = extensionField.getText();                   
                    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            JLabel searchLabel = new JLabel("Поиск...");
                            resultIF.add(searchLabel, BorderLayout.NORTH);
                            resultIF.show();
                            processFilesFromFolder(resultIF, catalog,
                            extension.substring(extension.lastIndexOf(".")), textListener);
                            if(foundFiles.isEmpty()){
                                resultIF.remove(searchLabel); 
                                resultIF.add(new JLabel("Файлы не найдены"), BorderLayout.CENTER);                           
                            }else{                                
                                resulList.setListData(foundFiles);
                                resultIF.remove(searchLabel);   
                                resultIF.add(resulList, BorderLayout.NORTH);
                                resultIF.updateUI();                       
                            }
                            return null;
                        }
                    };                
                    worker.execute();
                }catch(Exception ex){JOptionPane.showMessageDialog(null, 
                        "Не все поля заполнены либо заполненны не корректно");}
                foundFiles.clear();

            }
        });
                      
        getContentPane().add(myDesktop);            
        myDesktop.setVisible(true);        
        setJMenuBar(menuBar);       
        pack();
        setLocationRelativeTo(null);
        setVisible(true);       
    }
    
    public void processFilesFromFolder(JInternalFrame resultFrame, File folder,
            String filenameExtension, String searchText) throws IOException{      
        File[] folderEntries = folder.listFiles();
        JLabel otherFiles = new JLabel();
        for (File folderEntrie : folderEntries) {
            if (folderEntrie.isDirectory() && folderEntrie.list() != null) {
                processFilesFromFolder(resultFrame, folderEntrie, filenameExtension, searchText);
                continue;
            } else {
                if (folderEntrie.getName().endsWith(filenameExtension) && folderEntrie.isFile()) {
                    resultFrame.add(otherFiles = new JLabel(folderEntrie.getPath()), BorderLayout.CENTER);
                    resultFrame.updateUI();
                    FileInputStream inFile = new FileInputStream(folderEntrie);
                    byte[] str;
                    str = new byte[inFile.available()];
                    inFile.read(str);
                    String text = new String(str, "Cp1251");
                    if (text.contains(searchText)) {
                        foundFiles.add(folderEntrie.getPath());
                    }
                }
            }
        resultFrame.remove(otherFiles);    
        }        
    }
}