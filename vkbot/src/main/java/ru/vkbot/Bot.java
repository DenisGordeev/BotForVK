package ru.vkbot;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.GroupActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.messages.*;
import com.vk.api.sdk.objects.messages.Keyboard;
import com.vk.api.sdk.objects.messages.TemplateActionTypeNames;
import com.vk.api.sdk.objects.messages.KeyboardButtonAction;
import com.vk.api.sdk.queries.messages.MessagesGetLongPollHistoryQuery;

public class Bot {
    public static void main(String[] args) throws ClientException, ApiException, InterruptedException {
        TransportClient transportClient = new HttpTransportClient();
        VkApiClient vk = new VkApiClient(transportClient);
        Random random = new Random();
        Keyboard keyboard = new Keyboard().setOneTime(true);
        Keyboard kbd = new Keyboard();

        List<List<KeyboardButton>> allKey = new ArrayList<>();
        List<KeyboardButton> linel = new ArrayList<>();
        linel.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("Управление группой").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        allKey.add(linel);
        keyboard.setButtons(allKey);

        List<List<KeyboardButton>> alkt = new ArrayList<>();
        List<KeyboardButton> lt = new ArrayList<>();
        lt.add(new KeyboardButton().setAction(new KeyboardButtonAction().setLabel("2 слой кнопок :) ").setType(TemplateActionTypeNames.TEXT)).setColor(KeyboardButtonColor.POSITIVE));
        alkt.add(lt);
        kbd.setButtons(alkt);

        GroupActor actor = new GroupActor(groupid, "токен");
        Integer lol = vk.messages().getLongPollServer(actor).execute().getTs();
        while (true){
            MessagesGetLongPollHistoryQuery historyQuery = vk.messages().getLongPollHistory(actor).ts(lol);
            List<Message> messages = historyQuery.execute().getMessages().getItems();
            if (!messages.isEmpty()) {
                messages.forEach(message -> {
                    System.out.println(message.toString());
                    try {
                        if (message.getText().equals("Привет")) {
                            vk.messages().send(actor).message("Привет!").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else if (message.getText().equals("Как дела?")) {
                            vk.messages().send(actor).message("Отлично!").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                        else if (message.getText().equals("Управление")) {
                            vk.messages().send(actor).message("Вот: ").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(keyboard).execute();
                        }
                        else if (message.getText().equals("Управление группой")) {
                            vk.messages().send(actor).message("Держи: ").userId(message.getFromId()).randomId(random.nextInt(10000)).keyboard(kbd).execute();
                        }
                        else {
                            vk.messages().send(actor).message("Не понял.").userId(message.getFromId()).randomId(random.nextInt(10000)).execute();
                        }
                    }

                    catch(ApiException | ClientException e) {e.printStackTrace();}
                });
                lol = vk.messages().getLongPollServer(actor).execute().getTs();
                Thread.sleep(500);
            }



        }
    }
}
