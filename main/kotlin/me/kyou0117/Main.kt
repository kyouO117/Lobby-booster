package me.kyou0117

import org.bukkit.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Vector
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

import org.bukkit.util.StringUtil
import java.util.*


class Main: JavaPlugin(),Listener{
    //Enable
    override fun onEnable() {
        this.saveDefaultConfig()
        val config = config
        server.pluginManager.registerEvents(this, this)
    }
    //Command
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        return if (command.name.equals("lobby-booster", ignoreCase = true)) {
            if(args.isNotEmpty()){
                if(args[0] == "reload"){
                    reloadConfig()
                    config
                    sender.sendMessage("§aLobbyBooster: §econfig.ymlをリロードしました。")
                }else if(args[0] == "booster"){
                    when(args.size){
                        2 -> {
                            val p = Bukkit.getPlayer(args[1])
                            val pushheight = config.getInt("push-height")
                            val pushdistance = config.getInt("push-distance")
                            if(p != null) {
                                p.velocity = p.location.direction.multiply(pushdistance)
                                p.velocity = Vector(p.velocity.x, pushheight.toDouble(), p.velocity.z)
                            }else{
                                sender.sendMessage("§aLobbyBooster: §cプレイヤーが見つかりません。")
                            }
                        }
                        4 -> {
                            val p = Bukkit.getPlayer(args[1])
                            val pushheight = args[2].toInt()
                            val pushdistance = args[3].toInt()
                            if (p != null) {
                                p.velocity = p.location.direction.multiply(pushdistance)
                                p.velocity = Vector(p.velocity.x, pushheight.toDouble(), p.velocity.z)
                            } else {
                                sender.sendMessage("§aLobbyBooster: §cプレイヤーが見つかりません。")
                            }
                        }
                        else -> {
                            sender.sendMessage("§aLobbyBooster: §f/lobby-booster booster [player] <push-height> <push-distance>")
                            sender.sendMessage("§aLobbyBooster: §7([]は必須、<>は任意")
                        }
                    }
                }else{
                    sender.sendMessage("§aLobbyBooster: §c不明なコマンドです。")
                }
            }else{
                sender.sendMessage("§aLobbyBooster: §c引数を入力してください。")
            }
            true
        }else{
            true
        }
    }
    //Tab
    private val COMMANDS: Array<String> =
        arrayOf<String>("reload", "booster")

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> {
        if (command.name.equals("lobby-booster", ignoreCase = true)) {
            if (sender is Player && args.size == 1) {
                val p = sender
                val list = mutableListOf("", "reload", "booster")
                return list
            }
        }
        return emptyList()
    }
    //Event
    @EventHandler
    fun on(e: PlayerMoveEvent) {
        val world = config.getList("Worlds")
        val p = e.player
        val pw = p.world.name
        val location = p.location.block.blockData.material.toString()
        val location2 = p.location.block.getRelative(0, -1, 0)
        val pp = config.getString("PressurePlate")
        val configBlock = config.getString("Block")
        if (world != null) {
            if (world.contains(pw) && location == pp && location2.blockData.material.toString() == configBlock) {
                val pushdistance = config.getInt("push-distance")
                val pushheight = config.getInt("push-height")
                val soundpitch = config.getDouble("sound-pitch")
                val soundvolume = config.getInt("sound-volume")
                val sound = config.getString("Sound")
                p.velocity = p.location.direction.multiply(pushdistance)
                p.velocity = Vector(p.velocity.x, pushheight.toDouble(), p.velocity.z)
                p.playSound(p.location, Sound.valueOf(sound.toString()), soundvolume.toFloat(), soundpitch.toFloat())
                return
            }
        }
    }
}