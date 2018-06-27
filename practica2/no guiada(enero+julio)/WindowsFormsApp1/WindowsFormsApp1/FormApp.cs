using System;
using System.Windows.Forms;
using System.Collections;

namespace Cliente
{
    /// <summary>
    /// Form que contiene el panel del cliente
    /// </summary>
    public partial class FormApp : Form
    {
        // Instancia de la clase WSDl
        static WSDL serviceController = new WSDL();
        // Array con las sondas a las que se suscribe el cliente
        public ArrayList sondas = new ArrayList();

        /******** CONEXIÓN DIRECTA ****************************/
        private void button3_Click(object sender, EventArgs e)
        {
            bool isConnected = false;

            label6.Text = "Loading...";
            label6.Refresh();
            
            if (!textBox10.Text.Equals("") && !textBox6.Text.Equals(""))
            {
                isConnected = serviceController.Directa(textBox6.Text);
                WSDL.ID = Int32.Parse(textBox10.Text);
            }
            
            label6.Text = "Cargado!";
            label6.Refresh();

            if (isConnected)
                label6.Text = "Conectado";

            else
                label6.Text = "Error";

            label6.Refresh();

            WSDL.log("Conexión directa");
        }
        /******** BUSCAR SERVICIOS ****************************/
        private void button1_Click_1(object sender, EventArgs e)
        {
            label1.Text = "Cargando...";
            label1.Refresh();

            ArrayList serviceList;
            serviceList = serviceController.conectUDDI();

            label1.Text = "Loaded!";
            label1.Refresh();

            comboBox1.Items.Clear();

            String aux = "";
            foreach (String servicio in serviceList)
            {
                aux = servicio.Replace("localhost", "192.168.207.3");
                comboBox1.Items.Add(aux);
            }

            WSDL.log("Buscar servicios web en el servidor jUDDI");
        }
        /******** CONEXIÓN UDDI ******************************/
        private void button2_Click(object sender, EventArgs e)
        {
            String ruta = comboBox1.Text;
            String ip = textBox12.Text;

            if (!ruta.Equals("") && !textBox11.Text.Equals("") && !ip.Equals(""))
            {
                String service = comboBox1.SelectedItem.ToString().Replace("192.168.207.3", ip);

                bool aux1 = true;
                bool aux2 = true;
                if (service.Contains("sensor"))
                {
                    aux1 = serviceController.ConnectUDDIService(service, "sonda");
                }
                else if (service.Contains("actuador"))
                {
                    aux2 = serviceController.ConnectUDDIService(service, "actuador");
                }

                if (!aux1 || !aux2)
                {
                    label1.Text = "ERROR";
                }
                else
                {
                    label1.Text = "Hecho!!";
                    WSDL.ID = Int32.Parse(textBox11.Text);
                }
            }
            else
            {
                label1.Text = "Faltan campos";
            }
            label1.Refresh();

            WSDL.log("Conexión usando un servicio de jUDDI");
        }



        /******** SONDA GetTemperatura ****************************/
        private void button8_Click(object sender, EventArgs e)
        {
            textBox1.Text = WSDL.sensor.LeerDato("temperatura", WSDL.ID, true);
            WSDL.log("[SONDA]: Get Temperatura");
        }
        /******** SONDA getHumedad *********************************/
        private void button7_Click(object sender, EventArgs e)
        {
            textBox2.Text = WSDL.sensor.LeerDato("humedad", WSDL.ID, true);
            WSDL.log("[SONDA]: Get Humedad");
        }
        /******** SONDA set rangos ********************************/
        private void button14_Click(object sender, EventArgs e)
        {
            if (WSDL.ID == 0)
            {
                label19.Text = "ERROR. no conectado";
            }
            else if (textBox14.Text.Equals("") || textBox13.Text.Equals(""))
            {
                label19.Text = "ERROR. campos vacios";
            }
            else
            {
                WSDL.sensor.escribirFichero("rangoA", textBox13.Text, WSDL.ID, true);
                WSDL.sensor.escribirFichero("rangoB", textBox14.Text, WSDL.ID, true);
                WSDL.actuador.escribirFichero("rangoA", textBox13.Text, WSDL.ID, true);
                WSDL.actuador.escribirFichero("rangoB", textBox14.Text, WSDL.ID, true);

                label19.Text = "Hecho!!";
            }

            label19.Refresh();

            WSDL.log("[SONDA]: Set rango de valores");
        }



        /******** ACTUADOR getIncremento **************************/
        private void button6_Click(object sender, EventArgs e)
        {
            textBox3.Text = WSDL.actuador.LeerDato("incremento",WSDL.ID,true);
            textBox3.Refresh();
            WSDL.log("[ACTUADOR]: Get Incremento");
            
        }
        /******** ACTUADOR getEstado *******************************/
        private void button5_Click(object sender, EventArgs e)
        {
            textBox4.Text = WSDL.actuador.LeerDato("estado",WSDL.ID,true);
            textBox4.Refresh();
            WSDL.log("[ACTUADOR]: Get Estado");
        }
        /******** ACTUADOR setEstado *******************************/
        private void button3_Click_1(object sender, EventArgs e)
        {
            if (WSDL.actuador.LeerDato("suscrito",WSDL.ID,true).Equals("true"))
            {
                if (WSDL.actuador.LeerDato("estado",WSDL.ID,true).Equals("funciona"))
                    WSDL.actuador.escribirFichero("estado", "parado",WSDL.ID,true);
                else
                    WSDL.actuador.escribirFichero("estado", "funciona", WSDL.ID, true);

                label12.Text = "Hecho!!!";
            }
            else
            {
                label12.Text = "NO PROCEDE: no está suscrito a una sonda";
            }

            WSDL.log("[ACTUADOR]: Cambiar Estado");
        }
        /********** ACTUADOR Suscribira una sonda ********************/
        private void button10_Click(object sender, EventArgs e)
        {
            if (WSDL.sensor.LeerDato("actuador", WSDL.ID, true).Equals("false"))
            {
                WSDL.sensor.escribirFichero("actuador", "true", WSDL.ID, true);
                WSDL.actuador.escribirFichero("suscrito", "true", WSDL.ID, true);
                WSDL.actuador.escribirFichero("estado", "parado", WSDL.ID, true);

                label10.Text = "Hecho!!";
            }
            else
                label10.Text = "Ya estaba suscrito";

            label10.Refresh();

            WSDL.log("[ACTUADOR]: Suscribirse a una sonda");
        }
        /******* *ACUTADOR desuscribirse de la sonda *****************/
        private void button11_Click(object sender, EventArgs e)
        {
            if (WSDL.actuador.LeerDato("suscrito", WSDL.ID, true).Equals("false"))
            {
                label13.Text = "ERROR. no está suscrito a una sonda";
            }
            else
            {
                label13.Text = "Hecho!!";
                WSDL.sensor.escribirFichero("actuador", "false", WSDL.ID, true);
                WSDL.actuador.escribirFichero("suscrito", "false", WSDL.ID, true);
                WSDL.actuador.escribirFichero("estado", "parado", WSDL.ID, true);
            }

            label13.Refresh();

            WSDL.log("[ACTUADOR]: Desuscribirse de una sonda");
        }


        /******** CLIENTE suscribirse a una sonda *******************/
        private void button4_Click(object sender, EventArgs e)
        {
            if (textBox5.Text.Equals(""))
            {
                label8.Text = "ERROR campo vacio";
            }
            else if (!textBox5.Text.Equals("sonda"+WSDL.ID.ToString()))
            {
                label8.Text = "ERROR no conectado a la sonda";
            }
            else if (!sondas.Contains(textBox5.Text))
            {
                sondas.Add(textBox5.Text);

                WSDL.sensor.escribirFichero("cliente", "true", WSDL.ID, true);

                label8.Text = "Suscrito!!";


            }
            else
            {
                label8.Text = "Error: estaba registrada";
            }

            label8.Refresh();

            WSDL.log("[.NET]: Suscribirse a una sonda");
        }
        /******* CLIENTE Listar sondas suscritas *********************/
        private void button9_Click(object sender, EventArgs e)
        {
            String salida = "";

            if (sondas.Count == 0)
            {
                salida += "Aun no está suscrito a una sonda";
            }
            else
            {
                foreach (String sonda in sondas)
                {
                    salida += "Registrado a " + sonda + "\n\n";
                }
            }
            textBox7.Text = salida;
            textBox7.Refresh();

            WSDL.log("[.NET]: Listar suscripciones");
        }
        /******** CLIENTE desuscribirse de una sonda ****************/
        private void button12_Click(object sender, EventArgs e)
        {
            if (!textBox9.Text.Equals(""))
            {
                if (!textBox9.Text.Equals("sonda" + WSDL.ID.ToString()))
                {
                    label14.Text = "ERROR no conectado a la sonda";
                }
                else
                {
                    bool encontrado = false;
                    foreach (String sonda in sondas)
                    {
                        if (sonda.Equals(textBox9.Text))
                            encontrado = true;
                    }

                    if (encontrado)
                    {
                        WSDL.sensor.escribirFichero("cliente", "false", WSDL.ID, true);

                        sondas.Remove(textBox9.Text);

                        label14.Text = "Hecho!!";
                    }
                    else
                    {
                        label14.Text = "ERROR. no estaba suscrito";
                    }
                }
            }
            else
            {
                label14.Text = "ERROR. campo vacio";
            }

            label14.Refresh();

            WSDL.log("[.NET]: Desuscribirse de una sonda");
        }


        
        /***** LIMPIAR TEXTBOX Y LABELS ***************************/
        private void button13_Click(object sender, EventArgs e)
        {
            /* poner en blanco todos los textBox*/
            textBox1.Text = "";
            textBox2.Text = "";
            textBox3.Text = "";
            textBox4.Text = "";
            textBox5.Text = "";
            textBox6.Text = "192.168.207.3";
            textBox7.Text = "";
            textBox9.Text = "";
            textBox10.Text = "";
            textBox11.Text = "";
            textBox12.Text = "192.168.207.3";
            if (comboBox1.Items.Count != 0)
            {
                comboBox1.Items.Remove(1);
                comboBox1.Items.Remove(2);
            }
            /* poner en Estado los labels */
            label1.Text = "Estado";
            label6.Text = "Estado";
            label8.Text = "Estado";
            label10.Text = "Estado";
            label12.Text = "Estado";
            label13.Text = "Estado";
            label14.Text = "Estado";

            WSDL.log("[.NET]: Borrar texto del formulario");
        }
        private void button1_Click(object sender, EventArgs e)
        {

        }

        public FormApp()
        {
            InitializeComponent();
        }
        private void InitializeComponent()
        {
            this.ConectarUDDI = new System.Windows.Forms.Button();
            this.ConectarDirecta = new System.Windows.Forms.Button();
            this.getEstado = new System.Windows.Forms.Button();
            this.getIncremento = new System.Windows.Forms.Button();
            this.getHumedad = new System.Windows.Forms.Button();
            this.getTemperatura = new System.Windows.Forms.Button();
            this.comboBox1 = new System.Windows.Forms.ComboBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.textBox2 = new System.Windows.Forms.TextBox();
            this.textBox3 = new System.Windows.Forms.TextBox();
            this.textBox4 = new System.Windows.Forms.TextBox();
            this.textBox6 = new System.Windows.Forms.TextBox();
            this.label6 = new System.Windows.Forms.Label();
            this.BuscarServicios = new System.Windows.Forms.Button();
            this.SetEstado = new System.Windows.Forms.Button();
            this.SuscSonda = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.ListarSusc = new System.Windows.Forms.Button();
            this.label5 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.textBox5 = new System.Windows.Forms.TextBox();
            this.label8 = new System.Windows.Forms.Label();
            this.textBox7 = new System.Windows.Forms.TextBox();
            this.SuscActuador = new System.Windows.Forms.Button();
            this.label3 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label11 = new System.Windows.Forms.Label();
            this.label12 = new System.Windows.Forms.Label();
            this.DesuscActuador = new System.Windows.Forms.Button();
            this.label13 = new System.Windows.Forms.Label();
            this.DesuscribirCliente = new System.Windows.Forms.Button();
            this.textBox9 = new System.Windows.Forms.TextBox();
            this.label14 = new System.Windows.Forms.Label();
            this.textBox10 = new System.Windows.Forms.TextBox();
            this.label16 = new System.Windows.Forms.Label();
            this.textBox11 = new System.Windows.Forms.TextBox();
            this.label15 = new System.Windows.Forms.Label();
            this.textBox12 = new System.Windows.Forms.TextBox();
            this.LimpiarTexto = new System.Windows.Forms.Button();
            this.SetRango = new System.Windows.Forms.Button();
            this.textBox13 = new System.Windows.Forms.TextBox();
            this.textBox14 = new System.Windows.Forms.TextBox();
            this.label17 = new System.Windows.Forms.Label();
            this.label18 = new System.Windows.Forms.Label();
            this.label19 = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // ConectarUDDI
            // 
            this.ConectarUDDI.Location = new System.Drawing.Point(23, 105);
            this.ConectarUDDI.Name = "ConectarUDDI";
            this.ConectarUDDI.Size = new System.Drawing.Size(118, 23);
            this.ConectarUDDI.TabIndex = 2;
            this.ConectarUDDI.Text = "Conectar";
            this.ConectarUDDI.UseVisualStyleBackColor = true;
            this.ConectarUDDI.Click += new System.EventHandler(this.button2_Click);
            // 
            // ConectarDirecta
            // 
            this.ConectarDirecta.Location = new System.Drawing.Point(27, 218);
            this.ConectarDirecta.Name = "ConectarDirecta";
            this.ConectarDirecta.Size = new System.Drawing.Size(116, 23);
            this.ConectarDirecta.TabIndex = 4;
            this.ConectarDirecta.Text = "Conectar con IP";
            this.ConectarDirecta.UseVisualStyleBackColor = true;
            this.ConectarDirecta.Click += new System.EventHandler(this.button3_Click);
            // 
            // getEstado
            // 
            this.getEstado.Location = new System.Drawing.Point(452, 279);
            this.getEstado.Name = "getEstado";
            this.getEstado.Size = new System.Drawing.Size(101, 23);
            this.getEstado.TabIndex = 8;
            this.getEstado.Text = "getEstado";
            this.getEstado.UseVisualStyleBackColor = true;
            this.getEstado.Click += new System.EventHandler(this.button5_Click);
            // 
            // getIncremento
            // 
            this.getIncremento.Location = new System.Drawing.Point(452, 250);
            this.getIncremento.Name = "getIncremento";
            this.getIncremento.RightToLeft = System.Windows.Forms.RightToLeft.No;
            this.getIncremento.Size = new System.Drawing.Size(101, 23);
            this.getIncremento.TabIndex = 10;
            this.getIncremento.Text = "getIncremento";
            this.getIncremento.UseVisualStyleBackColor = true;
            this.getIncremento.Click += new System.EventHandler(this.button6_Click);
            // 
            // getHumedad
            // 
            this.getHumedad.Location = new System.Drawing.Point(453, 86);
            this.getHumedad.Name = "getHumedad";
            this.getHumedad.Size = new System.Drawing.Size(101, 23);
            this.getHumedad.TabIndex = 12;
            this.getHumedad.Text = "getHumedad";
            this.getHumedad.UseVisualStyleBackColor = true;
            this.getHumedad.Click += new System.EventHandler(this.button7_Click);
            // 
            // getTemperatura
            // 
            this.getTemperatura.Location = new System.Drawing.Point(453, 57);
            this.getTemperatura.Name = "getTemperatura";
            this.getTemperatura.Size = new System.Drawing.Size(101, 23);
            this.getTemperatura.TabIndex = 14;
            this.getTemperatura.Text = "getTemperatura";
            this.getTemperatura.UseVisualStyleBackColor = true;
            this.getTemperatura.Click += new System.EventHandler(this.button8_Click);
            // 
            // comboBox1
            // 
            this.comboBox1.FormattingEnabled = true;
            this.comboBox1.Location = new System.Drawing.Point(23, 78);
            this.comboBox1.Name = "comboBox1";
            this.comboBox1.Size = new System.Drawing.Size(296, 21);
            this.comboBox1.TabIndex = 15;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.ForeColor = System.Drawing.SystemColors.MenuHighlight;
            this.label1.Location = new System.Drawing.Point(150, 54);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(53, 13);
            this.label1.TabIndex = 16;
            this.label1.Text = "No usado";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(450, 32);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(66, 20);
            this.label4.TabIndex = 19;
            this.label4.Text = "Sensor";
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(590, 59);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(201, 20);
            this.textBox1.TabIndex = 21;
            // 
            // textBox2
            // 
            this.textBox2.Location = new System.Drawing.Point(590, 88);
            this.textBox2.Name = "textBox2";
            this.textBox2.Size = new System.Drawing.Size(201, 20);
            this.textBox2.TabIndex = 22;
            // 
            // textBox3
            // 
            this.textBox3.Location = new System.Drawing.Point(589, 253);
            this.textBox3.Name = "textBox3";
            this.textBox3.Size = new System.Drawing.Size(201, 20);
            this.textBox3.TabIndex = 23;
            // 
            // textBox4
            // 
            this.textBox4.Location = new System.Drawing.Point(589, 281);
            this.textBox4.Name = "textBox4";
            this.textBox4.Size = new System.Drawing.Size(201, 20);
            this.textBox4.TabIndex = 24;
            // 
            // textBox6
            // 
            this.textBox6.Location = new System.Drawing.Point(50, 192);
            this.textBox6.Name = "textBox6";
            this.textBox6.Size = new System.Drawing.Size(162, 20);
            this.textBox6.TabIndex = 26;
            this.textBox6.Text = "192.168.207.3";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label6.Location = new System.Drawing.Point(147, 223);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(40, 13);
            this.label6.TabIndex = 27;
            this.label6.Text = "Estado";
            // 
            // BuscarServicios
            // 
            this.BuscarServicios.Location = new System.Drawing.Point(23, 49);
            this.BuscarServicios.Name = "BuscarServicios";
            this.BuscarServicios.Size = new System.Drawing.Size(121, 23);
            this.BuscarServicios.TabIndex = 28;
            this.BuscarServicios.Text = "Buscar servicios";
            this.BuscarServicios.UseVisualStyleBackColor = true;
            this.BuscarServicios.Click += new System.EventHandler(this.button1_Click_1);
            // 
            // SetEstado
            // 
            this.SetEstado.Location = new System.Drawing.Point(452, 308);
            this.SetEstado.Name = "SetEstado";
            this.SetEstado.Size = new System.Drawing.Size(201, 23);
            this.SetEstado.TabIndex = 29;
            this.SetEstado.Text = "Cambiar estado";
            this.SetEstado.UseVisualStyleBackColor = true;
            this.SetEstado.Click += new System.EventHandler(this.button3_Click_1);
            // 
            // SuscSonda
            // 
            this.SuscSonda.Location = new System.Drawing.Point(18, 298);
            this.SuscSonda.Name = "SuscSonda";
            this.SuscSonda.Size = new System.Drawing.Size(130, 23);
            this.SuscSonda.TabIndex = 30;
            this.SuscSonda.Text = "Suscribirse a una sonda";
            this.SuscSonda.UseVisualStyleBackColor = true;
            this.SuscSonda.Click += new System.EventHandler(this.button4_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label2.Location = new System.Drawing.Point(23, 277);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(316, 20);
            this.label2.TabIndex = 31;
            this.label2.Text = "Administrar suscripciones cliente .NET";
            // 
            // ListarSusc
            // 
            this.ListarSusc.Location = new System.Drawing.Point(18, 330);
            this.ListarSusc.Name = "ListarSusc";
            this.ListarSusc.Size = new System.Drawing.Size(130, 23);
            this.ListarSusc.TabIndex = 33;
            this.ListarSusc.Text = "Listar suscripciones";
            this.ListarSusc.UseVisualStyleBackColor = true;
            this.ListarSusc.Click += new System.EventHandler(this.button9_Click);
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label5.Location = new System.Drawing.Point(21, 26);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(164, 20);
            this.label5.TabIndex = 34;
            this.label5.Text = "Conexión por UDDI";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label7.Location = new System.Drawing.Point(21, 162);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(143, 20);
            this.label7.TabIndex = 35;
            this.label7.Text = "Conexión directa";
            // 
            // textBox5
            // 
            this.textBox5.Location = new System.Drawing.Point(152, 301);
            this.textBox5.Name = "textBox5";
            this.textBox5.Size = new System.Drawing.Size(176, 20);
            this.textBox5.TabIndex = 32;
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label8.Location = new System.Drawing.Point(334, 304);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(40, 13);
            this.label8.TabIndex = 36;
            this.label8.Text = "Estado";
            // 
            // textBox7
            // 
            this.textBox7.Location = new System.Drawing.Point(152, 330);
            this.textBox7.Multiline = true;
            this.textBox7.Name = "textBox7";
            this.textBox7.Size = new System.Drawing.Size(176, 68);
            this.textBox7.TabIndex = 37;
            // 
            // SuscActuador
            // 
            this.SuscActuador.Location = new System.Drawing.Point(453, 337);
            this.SuscActuador.Name = "SuscActuador";
            this.SuscActuador.Size = new System.Drawing.Size(200, 23);
            this.SuscActuador.TabIndex = 38;
            this.SuscActuador.Text = "Suscribirse a sonda";
            this.SuscActuador.UseVisualStyleBackColor = true;
            this.SuscActuador.Click += new System.EventHandler(this.button10_Click);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(449, 221);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(82, 20);
            this.label3.TabIndex = 18;
            this.label3.Text = "Actuador";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label10.Location = new System.Drawing.Point(659, 342);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(40, 13);
            this.label10.TabIndex = 41;
            this.label10.Text = "Estado";
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Font = new System.Drawing.Font("Microsoft Tai Le", 11.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label11.Location = new System.Drawing.Point(23, 192);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(21, 19);
            this.label11.TabIndex = 42;
            this.label11.Text = "IP";
            // 
            // label12
            // 
            this.label12.AutoSize = true;
            this.label12.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label12.Location = new System.Drawing.Point(659, 311);
            this.label12.Name = "label12";
            this.label12.Size = new System.Drawing.Size(40, 13);
            this.label12.TabIndex = 43;
            this.label12.Text = "Estado";
            // 
            // DesuscActuador
            // 
            this.DesuscActuador.Location = new System.Drawing.Point(452, 366);
            this.DesuscActuador.Name = "DesuscActuador";
            this.DesuscActuador.Size = new System.Drawing.Size(200, 23);
            this.DesuscActuador.TabIndex = 44;
            this.DesuscActuador.Text = "Desuscribirse";
            this.DesuscActuador.UseVisualStyleBackColor = true;
            this.DesuscActuador.Click += new System.EventHandler(this.button11_Click);
            // 
            // label13
            // 
            this.label13.AutoSize = true;
            this.label13.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label13.Location = new System.Drawing.Point(659, 371);
            this.label13.Name = "label13";
            this.label13.Size = new System.Drawing.Size(40, 13);
            this.label13.TabIndex = 45;
            this.label13.Text = "Estado";
            // 
            // DesuscribirCliente
            // 
            this.DesuscribirCliente.Location = new System.Drawing.Point(15, 406);
            this.DesuscribirCliente.Name = "DesuscribirCliente";
            this.DesuscribirCliente.Size = new System.Drawing.Size(130, 23);
            this.DesuscribirCliente.TabIndex = 46;
            this.DesuscribirCliente.Text = "Desuscribirse";
            this.DesuscribirCliente.UseVisualStyleBackColor = true;
            this.DesuscribirCliente.Click += new System.EventHandler(this.button12_Click);
            // 
            // textBox9
            // 
            this.textBox9.Location = new System.Drawing.Point(152, 409);
            this.textBox9.Name = "textBox9";
            this.textBox9.Size = new System.Drawing.Size(176, 20);
            this.textBox9.TabIndex = 47;
            // 
            // label14
            // 
            this.label14.AutoSize = true;
            this.label14.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label14.Location = new System.Drawing.Point(334, 411);
            this.label14.Name = "label14";
            this.label14.Size = new System.Drawing.Size(40, 13);
            this.label14.TabIndex = 48;
            this.label14.Text = "Estado";
            // 
            // textBox10
            // 
            this.textBox10.Location = new System.Drawing.Point(275, 191);
            this.textBox10.Name = "textBox10";
            this.textBox10.Size = new System.Drawing.Size(39, 20);
            this.textBox10.TabIndex = 49;
            // 
            // label16
            // 
            this.label16.AutoSize = true;
            this.label16.Font = new System.Drawing.Font("Microsoft Tai Le", 11.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label16.Location = new System.Drawing.Point(234, 51);
            this.label16.Name = "label16";
            this.label16.Size = new System.Drawing.Size(24, 19);
            this.label16.TabIndex = 51;
            this.label16.Text = "ID";
            // 
            // textBox11
            // 
            this.textBox11.Location = new System.Drawing.Point(280, 50);
            this.textBox11.Name = "textBox11";
            this.textBox11.Size = new System.Drawing.Size(39, 20);
            this.textBox11.TabIndex = 52;
            // 
            // label15
            // 
            this.label15.AutoSize = true;
            this.label15.Font = new System.Drawing.Font("Microsoft Tai Le", 11.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label15.Location = new System.Drawing.Point(236, 191);
            this.label15.Name = "label15";
            this.label15.Size = new System.Drawing.Size(24, 19);
            this.label15.TabIndex = 53;
            this.label15.Text = "ID";
            // 
            // textBox12
            // 
            this.textBox12.Location = new System.Drawing.Point(153, 108);
            this.textBox12.Name = "textBox12";
            this.textBox12.Size = new System.Drawing.Size(162, 20);
            this.textBox12.TabIndex = 54;
            this.textBox12.Text = "192.168.207.3";
            // 
            // LimpiarTexto
            // 
            this.LimpiarTexto.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(0)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.LimpiarTexto.Location = new System.Drawing.Point(285, 453);
            this.LimpiarTexto.Name = "LimpiarTexto";
            this.LimpiarTexto.Size = new System.Drawing.Size(246, 75);
            this.LimpiarTexto.TabIndex = 55;
            this.LimpiarTexto.Text = "Borrar texto";
            this.LimpiarTexto.UseVisualStyleBackColor = true;
            this.LimpiarTexto.Click += new System.EventHandler(this.button13_Click);
            // 
            // SetRango
            // 
            this.SetRango.Location = new System.Drawing.Point(454, 132);
            this.SetRango.Name = "SetRango";
            this.SetRango.Size = new System.Drawing.Size(100, 46);
            this.SetRango.TabIndex = 56;
            this.SetRango.Text = "SetRango";
            this.SetRango.UseVisualStyleBackColor = true;
            this.SetRango.Click += new System.EventHandler(this.button14_Click);
            // 
            // textBox13
            // 
            this.textBox13.Location = new System.Drawing.Point(631, 131);
            this.textBox13.Name = "textBox13";
            this.textBox13.Size = new System.Drawing.Size(128, 20);
            this.textBox13.TabIndex = 58;
            // 
            // textBox14
            // 
            this.textBox14.Location = new System.Drawing.Point(631, 158);
            this.textBox14.Name = "textBox14";
            this.textBox14.Size = new System.Drawing.Size(128, 20);
            this.textBox14.TabIndex = 59;
            // 
            // label17
            // 
            this.label17.AutoSize = true;
            this.label17.Font = new System.Drawing.Font("Microsoft Tai Le", 11.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label17.Location = new System.Drawing.Point(560, 158);
            this.label17.Name = "label17";
            this.label17.Size = new System.Drawing.Size(65, 19);
            this.label17.TabIndex = 60;
            this.label17.Text = "Superior";
            // 
            // label18
            // 
            this.label18.AutoSize = true;
            this.label18.Font = new System.Drawing.Font("Microsoft Tai Le", 11.25F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label18.Location = new System.Drawing.Point(560, 132);
            this.label18.Name = "label18";
            this.label18.Size = new System.Drawing.Size(57, 19);
            this.label18.TabIndex = 61;
            this.label18.Text = "Inferior";
            // 
            // label19
            // 
            this.label19.AutoSize = true;
            this.label19.ForeColor = System.Drawing.SystemColors.Highlight;
            this.label19.Location = new System.Drawing.Point(628, 191);
            this.label19.Name = "label19";
            this.label19.Size = new System.Drawing.Size(40, 13);
            this.label19.TabIndex = 62;
            this.label19.Text = "Estado";
            // 
            // FormApp
            // 
            this.ClientSize = new System.Drawing.Size(809, 540);
            this.Controls.Add(this.label19);
            this.Controls.Add(this.label18);
            this.Controls.Add(this.label17);
            this.Controls.Add(this.textBox14);
            this.Controls.Add(this.textBox13);
            this.Controls.Add(this.SetRango);
            this.Controls.Add(this.LimpiarTexto);
            this.Controls.Add(this.textBox12);
            this.Controls.Add(this.label15);
            this.Controls.Add(this.textBox11);
            this.Controls.Add(this.label16);
            this.Controls.Add(this.textBox10);
            this.Controls.Add(this.label14);
            this.Controls.Add(this.textBox9);
            this.Controls.Add(this.DesuscribirCliente);
            this.Controls.Add(this.label13);
            this.Controls.Add(this.DesuscActuador);
            this.Controls.Add(this.label12);
            this.Controls.Add(this.label11);
            this.Controls.Add(this.label10);
            this.Controls.Add(this.SuscActuador);
            this.Controls.Add(this.textBox7);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.ListarSusc);
            this.Controls.Add(this.textBox5);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.SuscSonda);
            this.Controls.Add(this.SetEstado);
            this.Controls.Add(this.BuscarServicios);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.textBox6);
            this.Controls.Add(this.textBox4);
            this.Controls.Add(this.textBox3);
            this.Controls.Add(this.textBox2);
            this.Controls.Add(this.textBox1);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.comboBox1);
            this.Controls.Add(this.getTemperatura);
            this.Controls.Add(this.getHumedad);
            this.Controls.Add(this.getIncremento);
            this.Controls.Add(this.getEstado);
            this.Controls.Add(this.ConectarDirecta);
            this.Controls.Add(this.ConectarUDDI);
            this.Name = "FormApp";
            this.Text = "PANEL DEL CLIENTE ";
            this.Load += new System.EventHandler(this.FormApp_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }
        private void FormApp_Load(object sender, EventArgs e)
        {

        }
    }
}
