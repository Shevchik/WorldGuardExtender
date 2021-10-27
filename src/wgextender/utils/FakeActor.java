package wgextender.utils;

import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.auth.AuthorizationException;
import com.sk89q.worldedit.util.formatting.text.Component;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.Locale;
import java.util.UUID;

final class FakeActor implements Actor {

    FakeActor(){
    }

    @Override
    public String getName() {
        return Bukkit.getConsoleSender().getName();
    }
    @Override
    public UUID getUniqueId() {
        return null;
    }
    @Override
    public SessionKey getSessionKey() {
        return null;
    }
    @Override
    public boolean hasPermission(String arg0) {
        return true;
    }
    @Override
    public void checkPermission(String arg0) throws AuthorizationException {
    }
    @Override
    public String[] getGroups() {
        return null;
    }
    @Override
    public boolean canDestroyBedrock() {
        return false;
    }
    @Override
    public void dispatchCUIEvent(CUIEvent arg0) {
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
    @Override
    public File openFileOpenDialog(String[] arg0) {
        return null;
    }
    @Override
    public File openFileSaveDialog(String[] arg0) {
        return null;
    }
    @Override
    public void print(String arg0) {
    }
    @Override
    public void printDebug(String arg0) {
    }
    @Override
    public void printError(String arg0) {
    }
    @Override
    public void printRaw(String arg0) {
    }
    @Override
    public void print(Component arg0) {
    }
}
