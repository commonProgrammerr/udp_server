package com.redes.lib;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class UDPConnection {
  private int port;
  private byte[] buffer;
  private InetAddress targert_address;

  public DatagramSocket socket;
  public DatagramPacket _recived;
  public DatagramPacket _sended;

  UDPConnection(byte[] buffer) {
    this.buffer = buffer;
  }

  UDPConnection(byte[] buffer, int port) {
    this.port = port;
    this.buffer = buffer;
  }

  public void bootstrap() throws SocketException {
    this.socket = new DatagramSocket(this.port);
  }

  public void bootstrap(InetAddress _address) throws SocketException {
    this.targert_address = _address;
    this.socket = new DatagramSocket(port, targert_address);
  }

  public DatagramPacket recive() throws IOException {
    _recived = new DatagramPacket(buffer, buffer.length);
    this.socket.receive(_recived);
    return _recived;
  }

  public DatagramPacket send(byte[] data, InetAddress _address, int port) throws IOException {
    _sended = new DatagramPacket(data, data.length, targert_address, port);
    this.socket.send(_sended);
    return _sended;
  }

  public void close() {
    if (this.socket != null) {
      this.socket.close();
    }
  }

  public DatagramPacket send(byte[] data) throws IOException {
    return this.send(data, targert_address, port);
  }

  public byte[] getBuffer() {
    return buffer;
  }

  public int getPort() {
    return port;
  }

  public InetAddress getTargert_address() {
    return targert_address;
  }

}
