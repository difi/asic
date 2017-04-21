package no.difi.asic.model;

import no.difi.asic.lang.AsicExcepion;

import java.util.*;

/**
 * @author erlend
 */
public class Container {

    private Map<String, DataObject> dataObjects = new HashMap<>();

    private String rootFile;

    public String getRootFile() {
        return rootFile;
    }

    public void setRootFile(String rootFile) throws AsicExcepion {
        if (this.rootFile != null)
            throw new AsicExcepion("Root file is already set.");

        if (!dataObjects.containsKey(rootFile))
            throw new AsicExcepion(String.format("File '%s' is not known.", rootFile));

        this.rootFile = rootFile;
    }

    public void add(DataObject dataObject) {
        dataObjects.put(dataObject.getFilename(), dataObject);
    }

    public Collection<DataObject> getDataObjects() {
        return Collections.unmodifiableCollection(dataObjects.values());
    }
}
