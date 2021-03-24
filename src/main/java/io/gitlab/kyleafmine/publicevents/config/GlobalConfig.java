package io.gitlab.kyleafmine.publicevents.config;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import io.gitlab.kyleafmine.publicevents.PublicEvents;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class GlobalConfig {
    public static final int CURRENT_VERSION = 1;
    static GlobalConfig instance;
    public static GlobalConfig getConfig() {
        return instance;
    }
    int version;
    public ArrayList<String> firstPlaceCommands = new ArrayList<>();
    public ArrayList<String> secondPlaceCommands = new ArrayList<>();
    public ArrayList<String> thirdPlaceCommands = new ArrayList<>();

    /*
    Config format:
    int version;
    int firstCommands;
    String command - repeats firstCommands number of times;
    int secondCommands;
    String command - repeats secondCommands number of times;
    int thirdCommands;
    String command - repeats thirdCommands number of times;





     */

    public static void defaults() throws IOException {
        GlobalConfig tmp = new GlobalConfig();
        tmp.firstPlaceCommands.add("say %player% won first place!");
        tmp.secondPlaceCommands.add("say %player% won second place!");
        tmp.thirdPlaceCommands.add("say %player% won first place!");
        instance = tmp;
        instance.save();
    }
    public static void load() throws Exception {
        GlobalConfig tmp = new GlobalConfig();
        byte[] file = Files.toByteArray(PublicEvents.configFile);
        ByteArrayDataInput bb = ByteStreams.newDataInput(file);
        tmp.version = bb.readInt();

        if (tmp.version > CURRENT_VERSION) {
            throw new Exception("Current version is older than config version");
        }
        int firstCommands = bb.readInt();
        for (int x = 0; x < firstCommands; x++) {
            //Bukkit.getLogger().info("Read first command");
            tmp.firstPlaceCommands.add(bb.readUTF());
        }
        int secondCommands = bb.readInt();
        for (int x = 0; x < secondCommands; x++) {
            tmp.secondPlaceCommands.add(bb.readUTF());
        }
        int thirdCommands = bb.readInt();
        for (int x = 0; x < thirdCommands; x++) {
            tmp.thirdPlaceCommands.add(bb.readUTF());
        }
        instance = tmp;
    }
    public void save() throws IOException {
        ByteArrayDataOutput bb = ByteStreams.newDataOutput();
        bb.writeInt(CURRENT_VERSION);
        bb.writeInt(firstPlaceCommands.size());
        for (String s: firstPlaceCommands) {
            bb.writeUTF(s);
        }
        bb.writeInt(secondPlaceCommands.size());
        for (String s: secondPlaceCommands) {
            bb.writeUTF(s);
        }
        bb.writeInt(thirdPlaceCommands.size());
        for (String s: thirdPlaceCommands) {
            bb.writeUTF(s);
        }
        Files.write(bb.toByteArray(), PublicEvents.configFile);
    }
}
