package com.redes.lib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.regex.Pattern;

public class Server {

  private static int[] getNumbers(String regex, String data) throws NumberFormatException {
    String[] _numbers = data.replaceAll(" ", "").split(regex);
    int[] numbers = new int[2];
    numbers[0] = Integer.parseInt(_numbers[0]);
    numbers[1] = Integer.parseInt(_numbers[1]);
    return numbers;
  }

  private static byte[] getResponse(byte[] request) throws NumberFormatException {
    String requestString = request.toString();

    if (Pattern.matches("\\d+\\+\\d+", requestString)) {
      int[] num = Server.getNumbers("\\+", requestString);
      return Integer.toString(num[0] + num[1]).getBytes();
    } else if (Pattern.matches("\\d+\\-\\d+", requestString)) {
      int[] num = Server.getNumbers("\\-", requestString);
      return Integer.toString(num[0] - num[1]).getBytes();
    } else if (Pattern.matches("\\d+/\\d+", requestString)) {
      int[] num = Server.getNumbers("/", requestString);
      return Integer.toString(num[0] / num[1]).getBytes();
    } else {
      throw new Error("Invalid input");
    }
  }

  public static void start() {
    byte[] buffer = new byte[13];
    UDPConnection connection = new UDPConnection(buffer, 6789);
    try {

      while (true) {
        connection.bootstrap();

        if (connection.socket.isConnected()) {
          DatagramPacket request = connection.recive();
          System.out.println("New request recived: " + request.getAddress() + ":" + request.getPort());
          System.out.println("Data: " + request.getData().toString());

          try {
            byte[] response = Server.getResponse(request.getData());
            connection.send(response, request.getAddress(), request.getPort());
          } catch (NumberFormatException e) {
            e.printStackTrace();
            connection.send(e.getMessage().getBytes(), request.getAddress(), request.getPort());
          } catch (Error e) {
            e.printStackTrace();
            connection.send(e.getMessage().getBytes(), request.getAddress(), request.getPort());
          } // fechando try
        }
      }

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      connection.close();
    } // fechando o try
  }// fechando a main
}
// fechando a classe