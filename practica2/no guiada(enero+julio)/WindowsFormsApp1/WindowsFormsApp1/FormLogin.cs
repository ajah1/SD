using System;
using System.Threading;
using System.Windows.Forms;

namespace Cliente
{
    public partial class Login : Form
    {

        public Login()
        {
            InitializeComponent();
        }


        private void Login_Load(object sender, EventArgs e)
        {

        }


        private void LoginButton_Click(object sender, EventArgs e)
        {
            String username, password;

            username = textBoxUser.Text;
            password = textBoxPassword.Text;

            Console.WriteLine(username);
            Console.WriteLine(password);

            bool login = (username == "alex") && (password == "1234");


            if (textBoxPassword.Text.Equals("") || textBoxUser.Text.Equals(""))
            {
                label2.Text = "Campos vacios";
            }
            else if (!login)
            {
                label2.Text = "Campos erróneos";
            }
            else
            {
                FormApp f_app = new FormApp();
                Hide();
                f_app.Show();
            }

            WSDL.log("login is " + login);
        }
    }
}
