package br.com.daciosoftware.loteriasdms.notificacao;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import br.com.daciosoftware.loteriasdms.R;
import br.com.daciosoftware.loteriasdms.pojo.Sorteio;
import br.com.daciosoftware.loteriasdms.util.MyDateUtil;
import br.com.daciosoftware.loteriasdms.webservice.SorteioWebService;

/**
 * Created by fdacio on 20/01/17.
 */
public class SorteioService extends Service implements Runnable {

    private Context context;

    @Override
    public void run() {

        List<Sorteio> listaSorteio;

        try {

            listaSorteio = new SorteioWebService(context).getSorteiosAtualizar();

        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
            return;
        }

        //Aqui enviar uma notificação para o usuario informado
        //que há Novo Sorteio a ser atualizado
        if (listaSorteio.size() > 0) {

            String mensagemBarraStatus = "Novo Sorteio";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                int idNotificacao = R.string.app_name;
                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
                inboxStyle.setBigContentTitle(mensagemBarraStatus);

                for (Sorteio sorteio : listaSorteio) {
                    inboxStyle.addLine(sorteio.getNome());
                    inboxStyle.addLine("Concurso " + sorteio.getNumero() + "  de " + MyDateUtil.calendarToDateBr(sorteio.getData()));
                }

                new SorteioNotificacao(context).notificar(idNotificacao, inboxStyle);

            } else {
                for (Sorteio sorteio : listaSorteio) {
                    String titulo = sorteio.getNome();
                    String subtitulo = "Concurso " + sorteio.getNumero() + "  de " + MyDateUtil.calendarToDateBr(sorteio.getData());
                    int idNotificacao = 10000 * sorteio.getNumero();

                    new SorteioNotificacao(context).notificar(idNotificacao, mensagemBarraStatus, titulo, subtitulo);
                }
            }
        }


        stopSelf();
    }

    @Override
    public void onCreate() {
        this.context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
