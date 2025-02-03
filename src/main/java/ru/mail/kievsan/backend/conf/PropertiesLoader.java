package ru.mail.kievsan.backend.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {

    public static final String PROPS_FILEPATH = "./src/main/resources/application.properties";

    public static final String PROP_KEY_DATASOURCE_REPO_PATH = "datasource.repo.path";
    public static final String PROP_KEY_DATASOURCE_USERS_REPO_NAME = "datasource.repo.users";
    public static final String PROP_KEY_DATASOURCE_WALLETS_REPO_NAME = "datasource.repo.wallets";
    public static final String PROP_KEY_DATASOURCE_CATEGORIES_REPO_NAME = "datasource.repo.categories";

    public static final String PROP_KEY_DEFAULT_REPO_PATH = "./data/repo_files";
    public static final String PROP_KEY_DEFAULT_USERS_REPO_NAME = "users.json";
    public static final String PROP_KEY_DEFAULT_WALLETS_REPO_NAME = "wallets.json";
    public static final String PROP_KEY_DEFAULT_CATEGORIES_REPO_NAME = "categories.json";

    public static final String PROP_KEY_ADMIN_LOGIN = "admin.login";
    public static final String PROP_KEY_ADMIN_PASSWORD = "admin.password";

    public static final String PROP_KEY_DEFAULT_ADMIN_LOGIN = "superadmin";
    public static final String PROP_KEY_DEFAULT_ADMIN_PASSWORD = "123456";

    public static String loadDataSourcePath() {
       return loadProperties().getProperty(PROP_KEY_DATASOURCE_REPO_PATH, PROP_KEY_DEFAULT_REPO_PATH);
    }

    public static String loadUsersRepoName() {
        return loadProperties().getProperty(PROP_KEY_DATASOURCE_USERS_REPO_NAME, PROP_KEY_DEFAULT_USERS_REPO_NAME);
    }

    public static String loadWalletsRepoName() {
        return loadProperties().getProperty(PROP_KEY_DATASOURCE_WALLETS_REPO_NAME, PROP_KEY_DEFAULT_WALLETS_REPO_NAME);
    }

    public static String loadCategoriesRepoName() {
        return loadProperties().getProperty(PROP_KEY_DATASOURCE_CATEGORIES_REPO_NAME, PROP_KEY_DEFAULT_CATEGORIES_REPO_NAME);
    }

    public static String loadAdminLogin() {
        return loadProperties().getProperty(PROP_KEY_ADMIN_LOGIN, PROP_KEY_DEFAULT_ADMIN_LOGIN);
    }

    public static String loadAdminPassword() {
        return loadProperties().getProperty(PROP_KEY_ADMIN_PASSWORD, PROP_KEY_DEFAULT_ADMIN_PASSWORD);
    }

    public static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = new FileInputStream(PROPS_FILEPATH)) {
            props.load(input);  // Загружаем свойства из файла
        } catch (IOException e) {
            props.put(PROP_KEY_DATASOURCE_REPO_PATH, PROP_KEY_DEFAULT_REPO_PATH);
            props.put(PROP_KEY_DATASOURCE_USERS_REPO_NAME, PROP_KEY_DEFAULT_USERS_REPO_NAME);
            props.put(PROP_KEY_DATASOURCE_WALLETS_REPO_NAME, PROP_KEY_DEFAULT_WALLETS_REPO_NAME);
            props.put(PROP_KEY_DATASOURCE_CATEGORIES_REPO_NAME, PROP_KEY_DEFAULT_CATEGORIES_REPO_NAME);
            props.put(PROP_KEY_ADMIN_LOGIN, PROP_KEY_DEFAULT_ADMIN_LOGIN);
            props.put(PROP_KEY_ADMIN_PASSWORD, PROP_KEY_DEFAULT_ADMIN_PASSWORD);
        }
        return props;
    }
}
