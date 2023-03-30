import java.net.Socket;     //stores ip/port
import java.util.Scanner;   // read from file terminal
import java.io.PrintWriter; //write from a socket
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Client
{
    public static void main(String[] args) throws Exception{
        String IP = "";
        int port = 0;
        Scanner info=new Scanner(System.in);
        System.out.println("Enter an IP adress to connect to:");
        IP=info.nextLine();
        System.out.println("Enter a port number to connect to:");
        port=Integer.parseInt(info.nextLine());
        System.out.println("Connecting...");
        Socket s= new Socket(IP, port);
        System.out.println("Connection complete");
        Thread talk = new Thread(new TalkThread(s));
        talk.start();
        Thread listen = new Thread(new ListenThread(s));
        listen.start();
        talk.join();
        listen.join();
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
