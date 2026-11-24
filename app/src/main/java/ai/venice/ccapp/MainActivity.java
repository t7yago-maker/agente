package ai.venice.ccapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

  @Override protected void onCreate(Bundle b) {
    super.onCreate(b);
    ScrollView sc = new ScrollView(this);
    LinearLayout ll = new LinearLayout(this);
    ll.setOrientation(LinearLayout.VERTICAL);
    ll.setPadding(48, 96, 48, 48);
    ll.setBackgroundColor(Color.parseColor("#111418"));
    sc.addView(ll);

    TextView t = new TextView(this);
    t.setText("Agente");
    t.setTextColor(Color.WHITE);
    t.setTextSize(30);
    t.setPadding(0, 0, 0, 48);
    ll.addView(t);

    add(ll, "Abrir agente", "source ~/fcc/free.env; cd ~/projetos; exec claude");
    add(ll, "Abrir agente (autônomo)", "source ~/fcc/free.env; cd ~/projetos; exec claude --dangerously-skip-permissions");
    add(ll, "Terminal", "cd ~/projetos; exec bash");
    add(ll, "Atualizar Claude Code", "bash ~/fcc/update.sh; exec bash");
    add(ll, "Setup do Termux", "bash ~/fcc/apksetup.sh; exec bash");

    setContentView(sc);
  }

  void add(LinearLayout ll, String label, String cmd) {
    Button btn = new Button(this);
    btn.setText(label);
    btn.setAllCaps(false);
    btn.setOnClickListener(v -> run(cmd));
    LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
    p.bottomMargin = 20;
    ll.addView(btn, p);
  }

  void run(String cmd) {
    try {
      Intent i = new Intent();
      i.setClassName("com.termux", "com.termux.app.RunCommandService");
      i.setAction("com.termux.RUN_COMMAND");
      i.putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash");
      i.putExtra("com.termux.RUN_COMMAND_ARGUMENTS", new String[]{"-lc", cmd});
      i.putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home");
      i.putExtra("com.termux.RUN_COMMAND_BACKGROUND", false);
      i.putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0");
      startForegroundService(i);
    } catch (Exception e) {
      Toast.makeText(this, "Termux não encontrado ou sem allow-external-apps", Toast.LENGTH_LONG).show();
    }
  }
}
