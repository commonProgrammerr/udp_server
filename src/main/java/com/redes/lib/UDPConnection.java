package com.redes.lib;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;

public class UDPConnection extends DatagramSocket {
  private int port;
  private byte[] buffer;
  private InetAddress targert_address;

  public DatagramPacket _recived;
  public DatagramPacket _sended;

  UDPConnection(byte[] buffer) throws SocketException {
    super();
    this.buffer = buffer;
  }

  UDPConnection(byte[] buffer, int port) throws SocketException {
    super(port);
    this.port = port;
    this.buffer = buffer;
  }

  UDPConnection(byte[] _buffer, int port, InetAddress _address) throws SocketException {
    super(port, _address);
    this.buffer = _buffer;
    this.targert_address = _address;
  }

  public DatagramPacket clean(byte[] _buffer) throws IOException {
    DatagramPacket last_Packet = _recived;
    buffer = _buffer;
    _recived = new DatagramPacket(_buffer, _buffer.length);
    return last_Packet;
  }

  public DatagramPacket recive() throws IOException {
    _recived = new DatagramPacket(buffer, buffer.length);
    super.receive(_recived);
    return _recived;
  }

  public DatagramPacket send(byte[] data, InetAddress _address, int port) throws IOException {
    _sended = new DatagramPacket(data, data.length, targert_address, port);
    super.send(_sended);
    return _sended;
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
