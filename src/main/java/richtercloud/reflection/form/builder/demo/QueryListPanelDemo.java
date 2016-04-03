/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.GroupLayout;
import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.ReflectionFormPanel;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.MappingFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.jpa.JPACachedFieldRetriever;
import richtercloud.reflection.form.builder.jpa.panels.QueryListPanel;
import richtercloud.reflection.form.builder.message.LoggerMessageHandler;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 *
 * @author richter
 */
public class QueryListPanelDemo extends javax.swing.JFrame {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(QueryPanelDemo.class);
    private final static Random RANDOM = new Random();
    private static Long nextId = 1L;
    private final static String BIDIRECTIONAL_HELP_DIALOG_TITLE = String.format("%s - Info", JPAReflectionFormBuilderDemo.class.getSimpleName());
    private static synchronized Long getNextId() {
        nextId += 1;
        return nextId;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
            java.util.logging.Logger.getLogger(QueryListPanelDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QueryListPanelDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QueryListPanelDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QueryListPanelDemo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QueryListPanelDemo().setVisible(true);
            }
        });
    }
    private EntityManager entityManager;
    private ReflectionFormBuilder reflectionFormBuilder;
    private Class<?> entityClass = EntityA.class;
    private List<Object> initialValues = new LinkedList<>();
    private final MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
    private final QueryListPanel queryListPanel;
    private javax.swing.JButton createAButton;
    private javax.swing.JButton createBButton;
    private javax.swing.JButton createCButton;

    /**
     * Creates new form ListQueryPanelDemo
     */
    public QueryListPanelDemo() {
        this.entityManager = QueryPanelDemo.ENTITY_MANAGER_FACTORY.createEntityManager();
        MappingFieldHandlerFactory mappingFieldHandlerFactory = new MappingFieldHandlerFactory(messageHandler);
        FieldHandler fieldHandler = new MappingFieldHandler<>(mappingFieldHandlerFactory.generateClassMapping(),
                mappingFieldHandlerFactory.generatePrimitiveMapping());
        this.reflectionFormBuilder = new ReflectionFormBuilder("Field description",
                messageHandler,
                new JPACachedFieldRetriever());
        try {
            this.queryListPanel = new QueryListPanel(entityManager,
                reflectionFormBuilder,
                entityClass,
                messageHandler,
                this.initialValues,
                BIDIRECTIONAL_HELP_DIALOG_TITLE);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(this, //parent
                    String.format("The following unexpected exception occured during intialization of the query panel: %s", ReflectionFormPanel.generateExceptionMessage(ex)),
                    null, WIDTH);
            throw new RuntimeException(ex);
        }
        this.initComponents();
    }


    private void initComponents() {

        createAButton = new javax.swing.JButton();
        createCButton = new javax.swing.JButton();
        createBButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBounds(new java.awt.Rectangle(0, 0, 500, 300));

        createAButton.setText("Create new A (references B)");
        createAButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createAButtonActionPerformed(evt);
            }
        });

        createCButton.setText("Create new C (extends A)");
        createCButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createCButtonActionPerformed(evt);
            }
        });

        createBButton.setText("Create new B");
        createBButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createBButtonActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(queryListPanel)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(createAButton)
                        .addComponent(createBButton)
                        .addComponent(createCButton)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(queryListPanel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(createAButton)
                        .addComponent(createBButton)
                        .addComponent(createCButton)));

        pack();
    }

    private void createAButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Long nextId0 = getNextId();
        EntityA newA = new EntityA(nextId0, RANDOM.nextInt(), String.valueOf(RANDOM.nextInt()));
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(newA);
        this.entityManager.getTransaction().commit();
        LOGGER.info("Create and persisted new instance of {}", EntityA.class.getName());
    }

    private void createCButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Long nextId0 = getNextId();
        EntityC newC = new EntityC(nextId0, RANDOM.nextInt(), String.valueOf(RANDOM.nextInt()), String.valueOf(RANDOM.nextInt()));
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(newC);
        this.entityManager.getTransaction().commit();
        LOGGER.info("Create and persisted new instance of {}", EntityC.class.getName());
    }

    private void createBButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Long nextId0 = getNextId();
        CriteriaQuery<EntityA> criteriaQuery = this.entityManager.getCriteriaBuilder().createQuery(EntityA.class);
        Root<EntityA> queryRoot = criteriaQuery.from(EntityA.class);
        criteriaQuery.select(queryRoot);
        List<EntityA> as = this.entityManager.createQuery(criteriaQuery).getResultList();
        EntityA randomA = null;
        if(!as.isEmpty()) {
            randomA = as.get(RANDOM.nextInt(as.size()));
        }
        EntityB newB = new EntityB(nextId0, RANDOM.nextInt(), randomA);
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(newB);
        this.entityManager.getTransaction().commit();
        LOGGER.info("Create and persisted new instance of {}", EntityB.class.getName());
    }
}
