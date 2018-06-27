using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Listener;

namespace Alertas
{
    class Program
    {
        static void Main(string[] args)
        {
            MyTcpListener listener = new MyTcpListener();
            listener.StartServer();
        }
    }
}
