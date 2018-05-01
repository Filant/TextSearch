/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test1searchtext;

import java.io.File;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Anton
 */
public class MyTreeNode extends DefaultMutableTreeNode{
    boolean loaded = false;
    File file;
    String fullPathName;
  
    public MyTreeNode(String fullPathName, String nameUSerObject, boolean allowsChildren) {
        add(new DefaultMutableTreeNode("Loading...", false));
        setAllowsChildren(allowsChildren);
        setUserObject(nameUSerObject);
        this.fullPathName = fullPathName;
    }

  @Override
  public boolean isLeaf() {
    return false;
  }

  public void loadChildren(final DefaultTreeModel model, File []rootFiles) {
    if (loaded) {
      return;
    }
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {        
        @Override
        protected Void doInBackground() throws Exception {            
            removeAllChildren();    
            if(MyTreeNode.this.getParent() == null){
                for(File file: rootFiles){
                    add(new MyTreeNode(file.getPath(), file.getPath(), true));
                }
            }else{
                for(File file: rootFiles){
                    if(!file.isHidden()){ 
                        add(file.isDirectory() ? new MyTreeNode(file.getPath(), file.getName(), true):
                        new DefaultMutableTreeNode(file.getName(), false));
                    }
                }
            }
        return null;
      }

      @Override
      protected void done() {
        try {                
          model.nodeStructureChanged(MyTreeNode.this);
          loaded = true;
        } catch (Exception e) {
          e.printStackTrace();
        }
        super.done();
      }
    };
    worker.execute();
  }
  
    public String getFullPathName(){
        return fullPathName;
    }
    public void setLoaded(boolean load){
        loaded = load;
    } 
}
