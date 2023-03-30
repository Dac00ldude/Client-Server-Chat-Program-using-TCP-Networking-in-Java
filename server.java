import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.io.PrintWriter;
import java.util.Scanner;

public class server
{
    public static void main(String[] args) throws Exception{
        ServerSocket s=new ServerSocket(50000);//creates a new server using the port number 50000, usually unused
        System.out.println("My IP is: "+InetAddress.getLocalHost().getHostAddress());
        System.out.println("Wating for a connection");
        Socket sc=s.accept(); //waits until people add messages to server
        System.out.println("Someone connected");
        Thread talk = new Thread(new TalkThread(sc));
        talk.start();
        Thread listen = new Thread(new ListenThread(sc));
        listen.start();
        talk.join();
        listen.join();
        sc.close();
        while (true){}
    }
    private static class TalkThread implements Runnable{
        private Socket sc;
        public TalkThread(Socket sc){
            this.sc=sc;
        }

        @Override
        public void run(){
            try{
                PrintWriter pw=new PrintWriter(sc.getOutputStream(),true);
                Scanner scan=new Scanner(System.in);
                while(sc.isClosed()==false){
                    String msg=scan.nextLine();
                    pw.println(msg);
                }
            }
            catch (Exception e){
                //
            }
        }
    }
    private static class ListenThread implements Runnable{
        private Socket sc;
        public ListenThread(Socket sc){
            this.sc=sc;
        }

        @Override
        public void run(){
            try{
                InputStreamReader isr = new InputStreamReader(sc.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while(sc.isClosed()==false){
                    String msg=br.readLine();
                    if(msg==null){sc.close(); System.out.println("Someone Disconnected!");}
                    else{
                        System.out.println(msg);
                    }
                }
            }
            catch (Exception e){
                //
            }
        }
    }
}
