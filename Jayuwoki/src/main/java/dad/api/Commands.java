package dad.api;

import dad.api.models.Player;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Commands extends ListenerAdapter {

    private final ArrayList<String> roles = new ArrayList<>(List.of("Top", "Jungla", "Mid", "ADC", "Support"));
    private  ObservableList<Player> players = new SimpleListProperty<>(FXCollections.observableArrayList());

    public ArrayList<String> getRoles() {
        return roles;
    }

    public ObservableList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ObservableList<Player> players) {
        this.players = players;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Get the menssage content
        System.out.println("Mensaje recibido");
        String message = event.getMessage().getContentRaw().toLowerCase();
        System.out.println(message);
        if (message.startsWith("$")) {
            String[] comando = message.split(" ");
            // Switch with all the posible commands
            switch (comando[0]) {
                case "$privadita":
                    // Check if the command has the correct number of players
                    if (comando.length == 11) {
                        StartPrivadita(comando, event);
                    } else {
                        event.getChannel().sendMessage("El comando $privadita necesita 10 jugadores").queue();
                    }

            }
        }
    }

    private void StartPrivadita(String[] comando, MessageReceivedEvent event) {
        String[] playersNames = Arrays.copyOfRange(comando, 1, comando.length);
        for (String name : playersNames) {
            Player player = new Player();
            player.setName(name);
            players.add(player);
        }

        ObservableList<Player> blueTeam = new SimpleListProperty<>(FXCollections.observableArrayList());
        ObservableList<Player> redTeam = new SimpleListProperty<>(FXCollections.observableArrayList());

        // Shuffle the players so the teams are random
        Collections.shuffle(players);

        // Divide the players in two teams
        for (int i = 0; i < 5; i++) {
            blueTeam.add(players.get(i));
            redTeam.add(players.get(i + 5));
        }

        // Assign the roles to the players
        for (int i = 0; i < 5; i++) {
            blueTeam.get(i).setRole(roles.get(i));
            redTeam.get(i).setRole(roles.get(i));
        }

        // Create the message with the 2 teams
        StringBuilder messageBuilder = new StringBuilder();

        messageBuilder.append("```")
                .append("\nBlue Team\n");
        for (Player player : blueTeam) {
            messageBuilder.append(player.getPlayerName())
                    .append(" -> ")
                    .append(player.getRole())
                    .append("\n");
        }

        messageBuilder.append("\nRed Team\n");
        for (Player player : redTeam) {
            messageBuilder.append(player.getPlayerName())
                    .append(" -> ")
                    .append(player.getRole())
                    .append("\n");
        }


        messageBuilder.append("```");


        String formattedMessage = messageBuilder.toString();
        event.getChannel().sendMessage(formattedMessage).queue();

    }
}
