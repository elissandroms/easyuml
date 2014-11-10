package org.uml.explorer;

import java.awt.BorderLayout;
import java.util.Collection;
import java.util.HashMap;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.uml.model.ClassDiagram;
import org.uml.model.ComponentBase;

/**
 * Top component which displays something. See:
 *
 * http://platform.netbeans.org/tutorials/nbm-selection-1.html
 * http://platform.netbeans.org/tutorials/nbm-selection-2.html
 * http://platform.netbeans.org/tutorials/nbm-nodesapi3.html
 * http://wiki.netbeans.org/BasicUnderstandingOfTheNetBeansNodesAPI
 *
 *
 * http://netbeans-org.1045718.n5.nabble.com/TopComponent-associateLookup-is-incompatible-with-setActivatedNodes-is-it-a-bug-td3261230.html
 */
@ConvertAsProperties(
        dtd = "-//org.uml.explorer.ide.navigator//Explorer//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "ExplorerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.uml.explorer.ExplorerTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ExplorerAction",
        preferredID = "ExplorerTopComponent")
@Messages({
    "CTL_ExplorerAction=Explorer",
    "CTL_ExplorerTopComponent=Explorer Window",
    "HINT_ExplorerTopComponent=This is a Explorer window"
})
public final class ExplorerTopComponent extends TopComponent implements ExplorerManager.Provider, LookupListener {

    // was transient, why?
    private static ExplorerManager explorerManager = new ExplorerManager();
    private ClassDiagramNode cNode;
    private BeanTreeView explorerTree;

    private HashMap<Object, Node> objectsToNodes = new HashMap<>(); // mapping of object to corresponding node

    Result<ClassDiagram> resultCD;
    Result<ComponentBase> resultCDC;
    private boolean recursiveCall = false;

    public ExplorerTopComponent() {
        initComponents();

        setName(Bundle.CTL_ExplorerTopComponent());
        setToolTipText(Bundle.HINT_ExplorerTopComponent());

        explorerTree = new BeanTreeView();
        add(explorerTree, BorderLayout.CENTER);

        getActionMap().put("delete", ExplorerUtils.actionDelete(explorerManager, true));
        associateLookup(ExplorerUtils.createLookup(explorerManager, getActionMap()));
        
//        explorerManager.setRootContext(new AbstractNode(new CategoryChildren()));
//        explorerManager.getRootContext().setDisplayName("UML class diagram");  
        ((BeanTreeView) explorerTree).setRootVisible(false);
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return explorerManager;
    }

    public static ExplorerManager getStaticExplorerManager() {
        return explorerManager;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void componentOpened() {
        resultCD = Utilities.actionsGlobalContext().lookupResult(ClassDiagram.class);
        resultCD.addLookupListener(this);
        // zasto ovaj poziv? radi i bez toga, jer je registrovan lookup, sam zove resultChanged
//        resultChanged(new LookupEvent(resultCD));

        resultCDC = Utilities.actionsGlobalContext().lookupResult(ComponentBase.class);
        resultCDC.addLookupListener(this);
//        resultChanged(new LookupEvent(resultCdc));
    }

    @Override
    public void componentClosed() {
        resultCD.removeLookupListener(this);
        resultCDC.removeLookupListener(this);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Lookup.Result result = (Lookup.Result) ev.getSource();
        Collection<Object> instances = result.allInstances();

        if (!instances.isEmpty()) {
            for (Object selectedItem : instances) {
                if (selectedItem instanceof ClassDiagram) {
                    ClassDiagram selectedComponent = (ClassDiagram) selectedItem;
                    //this.setName(selectedComponent.getName() + " -  Explorer");
                    //if (cNode == null || !selectedComponent.equals(cNode.getClassDiagram())) {
                    cNode = new ClassDiagramNode(selectedComponent);
//                    recursiveCall = true;
                    explorerManager.setRootContext(cNode); //this one calls resultChanged recursivly, since global lookup is changed

//                    for (Node node : explorerManager.getRootContext().getChildren().getNodes())
//                        if (node instanceof ClassDiagramComponentNode)
//                            objectsToNodes.put(((ClassDiagramComponentNode) node).getComponent(), node);
//                        explorerManager.setExploredContextAndSelection(cNode, new Node[]{cNode});
                    explorerTree.setRootVisible(true);
                    //}
                }
//                else if (selectedItem instanceof ClassDiagramComponent) {
////                    ClassDiagramComponent selectedClassComponent = (ClassDiagramComponent) selectedItem;
////                    this.setName(selectedClassComponent.getName() + " -  Explorer");
////                    ClassDiagramComponentNode classComponentNode = new ClassDiagramComponentNode(selectedClassComponent);
////
////                    ((BeanTreeView) jScrollPane1).setRootVisible(true);
////                    recursiveCall = true;
////                    explorerManager.setRootContext(classComponentNode); //this one calls resultChanged recursivly, since global lookup is changed
////                    try {
////                        explorerManager.setExploredContextAndSelection(classComponentNode, new Node[]{classComponentNode});
////                        System.out.println("Promenio");
////                    } catch (PropertyVetoException ex) {
////                        Exceptions.printStackTrace(ex);
////                    }
//                    Node[] nodes = new Node[1];
//                    nodes[0] = objectsToNodes.get(selectedItem);
//                    if (nodes[0] != null) {
//                        try {
//                            recursiveCall = true;
//                            explorerManager.setSelectedNodes(nodes);
//                        } catch (PropertyVetoException ex) {
//                            Exceptions.printStackTrace(ex);
//                        }
//                    }
//                } else if (!recursiveCall) {
//                    explorerManager.setRootContext(Node.EMPTY);
//                    BeanTreeView btw = (BeanTreeView) jScrollPaneTree;
//                    btw.setRootVisible(false);
//                    this.setName("Explorer");
//                } else {
//                    recursiveCall = false;
//                }
            }
        } else {
            //explorerManager.setRootContext(new AbstractNode(null));
            //explorerTree.removeAll();
            //explorerTree.setRootVisible(false);
        }
//        else if (cNode != null) {
//            try {
//                explorerManager.setExploredContextAndSelection(cNode, new Node[]{cNode});
//            } catch (PropertyVetoException ex) {
//                Exceptions.printStackTrace(ex);
//            }
//        }
    }

}
//else if (selectedItem instanceof ClassWidget) {
//                    System.out.println("Klas vidzet");
//                    ClassWidget selectedClassWidget = (ClassWidget) selectedItem;
//                    this.setName(selectedClassWidget.getName() + " -  Explorer");
//                    ClassDiagramComponentNode classComponentNode = new ClassDiagramComponentNode(selectedClassWidget.getComponent());
//
//                    ((BeanTreeView) jScrollPane1).setRootVisible(true);
//                    recursiveCall = true;
//                    explorerManager.setRootContext(classComponentNode); //this one calls resultChanged recursivly, since global lookup is changed
//                    try {
//                        explorerManager.setExploredContextAndSelection(classComponentNode, new Node[]{classComponentNode});
//                    } catch (PropertyVetoException ex) {
//                        Exceptions.printStackTrace(ex);
//                    }
//                }