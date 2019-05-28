//from  w  w  w  . ja  v a  2  s.  c o m

import java.nio.ByteBuffer;

public class Main {
  public static String toBitString(final ByteBuffer b) {
    final char[] bits = new char[8 * b.position()];
    for(int i = 0; i < b.position(); i++) {
        final byte byteval = b.get(i);
        int bytei = i << 3;
        int mask = 0x1;
        for(int j = 7; j >= 0; j--) {
            final int bitval = byteval & mask;
            if(bitval == 0) {
                bits[bytei + j] = '0';
            } else {
                bits[bytei + j] = '1';
            }
            mask <<= 1;
        }
    }
    return String.valueOf(bits);
  }


  public static void main(String[] argv){

    ByteBuffer testPacket = ByteBuffer.allocate(100); // room for 100 bytes
    testPacket.putDouble(40.047591462658794);
    testPacket.putDouble(-76.31927490234376);
    System.out.println("capacity: " + testPacket.capacity() + "   position: " + testPacket.position() +   "  mark: " + testPacket.mark() + "   limit: " + testPacket.limit());
    String ans = toBitString(testPacket);

    // 0100 0000 0100 0100 0000 0110 0001 0111 0111 1010 0001 1111 1101 1000 0001 1011 1100000001010011000101000110111100000000000000000000000000000001
    System.out.println("ans: " + ans);
  }
}