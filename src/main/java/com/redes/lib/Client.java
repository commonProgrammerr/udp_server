package com.redes.lib;

import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;

public class Client {
  private UDPConnection connection;

  public void start(String[] args) {

    try {
      byte[] message = args[0].getBytes();// pegando o conteudo da mensagem do args
      InetAddress server_address = InetAddress.getByName(args[1]);// pegando o hostname do servidor

      System.out.println(message.length);
      System.out.println(server_address.getHostName());

      connection = new UDPConnection(new byte[13], 6789, server_address);
      connection.send(message);

      DatagramPacket reply = connection.recive();
      System.out.println("Reply: " + new String(reply.getData())); // imprimindo a resposta

    } catch (SocketException e) {
      e.printStackTrace();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      connection.close();
    } // fechando o try
  }// fechando a main
}// fechando a classe
