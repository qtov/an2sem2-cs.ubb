﻿using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace lab1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string connectionString = "Server = SYBIL2; Database = proj1; Integrated Security = True";
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {

                }
            }
            catch (SqlException ex)
            {
                Console.WriteLine(ex);
            }
        }
    }
}
