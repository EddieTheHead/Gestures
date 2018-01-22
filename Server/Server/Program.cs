using System;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.IO;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Collections;
using System.Threading;
using System.Diagnostics;

namespace Server
{
    class Program
    {
        static void Main(string[] args)
        {
           /* Socket listener, connecter, acc;
            listener = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
            connecter = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            listener.Bind(new IPEndPoint(IPAddress.Parse(GetLocalIPAddress()), 8888));
            listener.Listen(0);

            new Thread(() =>
            {
                acc=listener.Accept();
                Console.WriteLine("Connected");
            }).Start();
            connecter.Connect(IPAddress.Parse(GetLocalIPAddress()), 8888);*/

            
            Console.WriteLine("Connect with IP: " + GetLocalIPAddress());
            while (true)
            {

                Socket sck = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

                sck.Bind(new IPEndPoint(IPAddress.Parse(GetLocalIPAddress()), 8888));
                sck.Listen(0);
                Socket acc = sck.Accept();

                byte[] buffer = Encoding.Default.GetBytes("Hello World!");
                acc.Send(buffer, 0, buffer.Length, 0);

                buffer = new byte[255];
                int rec = acc.Receive(buffer, 0, buffer.Length, 0);

                Array.Resize(ref buffer, rec);

                string zmienna = Encoding.Default.GetString(buffer);
                Console.WriteLine("Received: {0}", zmienna);

                if (zmienna == "p\n")
                {
                    SendKeys.SendWait("{LEFT}");
                    Console.WriteLine("it means: go to previous slide");
                }
                else if (zmienna == "n\n") 
                {
                    SendKeys.SendWait("{RIGHT}");
                    Console.WriteLine("it means: go to next slide");
                }

                sck.Close();
                acc.Close();
            }

            Console.Read();

        }
        

        public static string GetLocalIPAddress()
        {
            var host = Dns.GetHostEntry(Dns.GetHostName());
            foreach (var ip in host.AddressList)
            {
                if (ip.AddressFamily == AddressFamily.InterNetwork)
                {
                    return ip.ToString();
                }
            }
            throw new Exception("No network adapters with an IPv4 address in the system!");
        }
    }
}