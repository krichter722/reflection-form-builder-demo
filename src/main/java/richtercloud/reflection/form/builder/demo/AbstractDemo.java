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

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.message.handler.ConfirmMessageHandler;
import richtercloud.message.handler.DialogConfirmMessageHandler;
import richtercloud.message.handler.LoggerMessageHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.jpa.idapplier.GeneratedValueIdApplier;
import richtercloud.reflection.form.builder.jpa.idapplier.IdApplier;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorage;
import richtercloud.reflection.form.builder.jpa.storage.DerbyEmbeddedPersistenceStorageConf;
import richtercloud.reflection.form.builder.jpa.storage.PersistenceStorage;
import richtercloud.reflection.form.builder.storage.StorageConfValidationException;
import richtercloud.reflection.form.builder.storage.StorageCreationException;

/**
 *
 * @author richter
 */
public abstract class AbstractDemo extends JFrame {
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractDemo.class);
    private final Connection connection;
    private final MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
    private final ConfirmMessageHandler confirmMessageHandler = new DialogConfirmMessageHandler(this);
    private final IdApplier idApplier = new GeneratedValueIdApplier();
    private final String databaseName;
    private final File parentDir;
    private final PersistenceStorage storage;
    private final Set<Class<?>> entityClasses;
    private final File schemeChecksumFile;

    public AbstractDemo() throws SQLException, IOException, StorageConfValidationException, StorageCreationException {
        this.parentDir = new File("/tmp/reflection-form-builder-demo");
        if(!parentDir.exists()) {
            parentDir.mkdir();
        }
        String databaseDirName = "databases";
        this.databaseName = new File(parentDir, databaseDirName).getAbsolutePath();

        try {
            Class<?> driver = EmbeddedDriver.class;
            driver.newInstance();
            connection = DriverManager.getConnection(String.format("jdbc:derby:%s;create=%s",
                    databaseName,
                    !new File(databaseName).exists()));
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    LOGGER.info("running {} shutdown hooks", QueryPanelDemo.class);
                    if(connection != null) {
                        try {
                            connection.close();
                        } catch (SQLException ex) {
                            LOGGER.error("an exception during shutdown of the database connection occured", ex);
                        }
                    }
                }
            });
        } catch (InstantiationException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, //parentComponent
                    String.format("<html>An unexpected exception occured during the initialization of resources (%s)</html>", ex.getMessage()), //message
                    String.format("Error in initialization of resources - %s", getAppName()), //title
                    JOptionPane.ERROR_MESSAGE //type
            );
            LOGGER.error("An unexpected exception occured during the initialization of resources (see nested exception for details)", ex);
            throw new ExceptionInInitializerError(ex);
        }
        this.entityClasses = new HashSet<Class<?>>(Arrays.asList(EntityA.class,
                EntityB.class,
                EntityC.class,
                EntityD.class));
        this.schemeChecksumFile = new File(getParentDir(), "scheme-checksum");
        this.storage = new DerbyEmbeddedPersistenceStorage(new DerbyEmbeddedPersistenceStorageConf(entityClasses,
                getDatabaseName(),
                schemeChecksumFile),
                "richtercloud_reflection-form-builder-demo_jar_1.0-SNAPSHOTPU",
                1 //parallelQuerycount
        );
    }

    protected abstract String getAppName();

    public File getParentDir() {
        return parentDir;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public IdApplier getIdApplier() {
        return idApplier;
    }

    public ConfirmMessageHandler getConfirmMessageHandler() {
        return confirmMessageHandler;
    }

    public PersistenceStorage getStorage() {
        return storage;
    }

    public Set<Class<?>> getEntityClasses() {
        return entityClasses;
    }

    public File getSchemeChecksumFile() {
        return schemeChecksumFile;
    }
}
