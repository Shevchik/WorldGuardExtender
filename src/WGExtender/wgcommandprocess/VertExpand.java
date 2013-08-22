package WGExtender.wgcommandprocess;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class VertExpand {

	protected static void expand(WorldEditPlugin we, Player pl)
	{
		Selection psel = we.getSelection(pl);
		if (psel == null)
		{
			return;
		}
		else
		{
			Bukkit.dispatchCommand(pl, "/expand vert");
		}
	}
	
}
