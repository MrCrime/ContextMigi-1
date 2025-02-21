package com.github.migi_1.Context.utility;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;

/**
 * This class makes the asset manager accessible from any class, after it has been initialized.
 *
 * SINGLETON class
 * @author Marcel
 *
 */
public final class ProjectAssetManager {



    /**
     * Instance of the Asset Manager.
     */
    private AssetManager assetManager;

    /**
     * Singleton instance.
     */
    private static final ProjectAssetManager INSTANCE = new ProjectAssetManager();

    /**
     * Private singleton constructor.
     */
    private ProjectAssetManager() { };

    /**
     * Singleton initialisation method.
     * @return newly created instance
     */
    public static ProjectAssetManager getInstance() {
        return INSTANCE;

    }

    /**
     * Return the asset manager.
     * @return Instance Manager instance.
     */
    public AssetManager getAssetManager() {
        return assetManager;
    }

    /**
     * Set a new Asset Manager instance.
     * @param assetManager new Asset Manager instance
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        this.assetManager.registerLocator("assets", FileLocator.class);
    }

}
