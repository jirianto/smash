package edu.berkeley.cs.amplab.smash4j;

import com.google.common.base.Optional;

import edu.berkeley.cs.amplab.smash4j.CommandDispatcher.MainCommand;

import java.io.File;
import java.util.prefs.Preferences;

public class Main {

  static final Preferences
      PREFERENCES = Preferences.userNodeForPackage(Main.class);
  static final String
      PREFERENCES_PATH = PREFERENCES.absolutePath(),
      PROGRAM_NAME = "AMPLab-SMaSH4J/0.1";

  private static void main(MainCommand command) throws Exception {
  }

  public static void main(String[] args) throws Exception {
    new CommandDispatcher() {

      @Override protected void main(MainCommand command) throws Exception {
        Main.main(command);
      }

      @Override protected void setPrefs(CommandDispatcher.SetPrefsCommand command) {
        Optional<CommandDispatcher.SetPrefsCommand.AuthorizationMethod>
            authorizationMethod = command.authorizationMethod();
        if (authorizationMethod.isPresent()) {
          PREFERENCES.put("authorizationMethod", authorizationMethod.get().toString());
        }
        Optional<String> apiKey = command.apiKey();
        if (apiKey.isPresent()) {
          PREFERENCES.put("apiKey", apiKey.get());
        }
        Optional<File> clientSecretsFile = command.clientSecretsFile();
        if (clientSecretsFile.isPresent()) {
          PREFERENCES.put("clientSecretsFile", clientSecretsFile.get().getPath());
        }
        Optional<String> serviceAccountId = command.serviceAccountId();
        if (serviceAccountId.isPresent()) {
          PREFERENCES.put("serviceAccountId", serviceAccountId.get());
        }
        Optional<File> serviceAccountP12File = command.serviceAccountP12File();
        if (serviceAccountP12File.isPresent()) {
          PREFERENCES.put("serviceAccountP12File", serviceAccountP12File.get().getPath());
        }
      }

      private void showPref(boolean condition, String key) {
        if (condition) {
          String value = PREFERENCES.get(key, null);
          System.out.format("%s:%s = %s%n",
              PREFERENCES_PATH, key, null == value ? null : String.format("\"%s\"", value));
        }
      }

      @Override protected void showPrefs(CommandDispatcher.ShowPrefsCommand command) {
        boolean
            authorizationMethod = command.authorizationMethod(),
            apiKey = command.apiKey(),
            clientSecretsFile = command.clientSecretsFile(),
            serviceAccountId = command.serviceAccountId(),
            serviceAccountP12File = command.serviceAccountP12File(),
            noFlags = !(authorizationMethod
                || apiKey
                || clientSecretsFile
                || serviceAccountId
                || serviceAccountP12File);
        showPref(authorizationMethod || noFlags, "authorizationMethod");
        showPref(apiKey || noFlags, "apiKey");
        showPref(clientSecretsFile || noFlags, "clientSecretsFile");
        showPref(serviceAccountId || noFlags, "serviceAccountId");
        showPref(serviceAccountP12File || noFlags, "serviceAccountP12File");
      }
    }.parse(args);
  }
}
