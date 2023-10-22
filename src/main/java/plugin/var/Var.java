package plugin.var;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import jdk.jshell.spi.ExecutionControl.InternalException;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Var extends JavaPlugin implements Listener {

    private  int count;

    private  final  Object lockObject = new Object();
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        //  配列、リスト、map
        String[] stringArray = new String[]{"test1", "test2", "test3"};

        List<String> stringList = List.of("test1", "test2", "test3");

        stringList.add("test4");


        Map<Integer, List<String>> map = new HashMap<>();

        map.put(1, (List.of("test1", "test2")));

        map.put(2, List.of("test3", "test4"));

        List<String> value = map.get(1);


    }




    /**
     * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
     *
     * @param e イベント
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws InternalException {
        // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。
        Player player = e.getPlayer();
        World world = player.getWorld();

        List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);

        if(count % 2 == 0) {
//            ２秒間隔空けて以下の花火の実行する
            synchronized (lockObject) {
                try {
                    lockObject.wait(2000);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }

            for (Color color : colorList) {
                // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
                Firework firework = world.spawn(player.getLocation(), Firework.class);

                // 花火オブジェクトが持つメタ情報を取得。
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                // メタ情報に対して設定を追加したり、値の上書きを行う。
                // 今回は青色で星型の花火を打ち上げる。
                fireworkMeta.addEffect(
                    FireworkEffect.builder()
                        .withColor(Color.BLUE)
                        .withColor(Color.YELLOW)
                        .with(Type.BALL_LARGE)
                        .withFlicker()
                        .build());
                fireworkMeta.setPower(1 + 2);

                //文字列変数の値のtestをTestという大文字にする
                String test = "test";

                test.toUpperCase().repeat(3);

                // 追加した情報で再設定する。
                firework.setFireworkMeta(fireworkMeta);
            }
        }
        count++;
    }
}

