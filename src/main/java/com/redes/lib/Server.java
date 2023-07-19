package com.redes.lib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.regex.Pattern;

public class Server extends Thread {
  private UDPConnection connection;

  private static int[] getNumbers(String regex, String data) throws NumberFormatException {
    String[] _numbers = data.replaceAll(" ", "").split(regex);
    int[] numbers = new int[2];
    numbers[0] = Integer.parseInt(_numbers[0]);
    numbers[1] = Integer.parseInt(_numbers[1]);
    return numbers;
  }

  /**
   * @param request
   * @return
   * @throws NumberFormatException
   * 
   *                               Trata a requisição e devolve a resposta
   */
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

  public void bootstrap() throws SocketException {
    // inicia o servidor na porta 6789
    connection = new UDPConnection(new byte[13], 6789);

    try {
      while (true) {
        // caso haja conexção e dados recebidos inicia uma nova thread como o
        // processamento
        if (connection.isConnected() && connection.recive().getLength() > 0) {
          this.start();
          sleep(10); // pequeno delay para evitar concorrencia
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      connection.close();
    } // fechando o try
  }// fechando a main

  public void run() {
    try {
      DatagramPacket request = connection.clean(new byte[13]); // Modifica o DatagramPacket por um novo e retorna o que
                                                               // contem a requisição

      System.out.println("New request recived: " + request.getAddress() + ":" + request.getPort());
      System.out.println("Data: " + request.getData().toString());

      try {
        byte[] response = Server.getResponse(request.getData()); // obtem a resposta do calculo...
        connection.send(response, request.getAddress(), request.getPort()); // envia a resposta
      } catch (NumberFormatException e) {
        e.printStackTrace();
        connection.send(e.getMessage().getBytes(), request.getAddress(), request.getPort()); // envia erro para o client
      } catch (Error e) {
        e.printStackTrace();
        connection.send(e.getMessage().getBytes(), request.getAddress(), request.getPort()); // envia erro para o client
      } // fechando try

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (connection != null)
        connection.close();
    } // fechando o try
  }// fechando a main
}
// fechando a classe