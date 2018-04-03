using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace proj1
{
    public partial class Form1 : Form
    {
        private SqlDataAdapter adapterUsers;
        private SqlDataAdapter adapterComments;
        private DataSet dataSet;
        private SqlCommandBuilder cb;
        private BindingSource bsUsers;
        private BindingSource bsComments;
        private SqlConnection conn;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            conn = new SqlConnection("Data Source=SIBYL2;Initial Catalog=proj1;Integrated Security=True");
            dataSet = new DataSet();
            adapterUsers = new SqlDataAdapter("select * from users", conn);
            adapterComments = new SqlDataAdapter("select * from comments", conn);
            cb = new SqlCommandBuilder(adapterUsers);
            adapterUsers.Fill(dataSet, "Users");
            adapterComments.Fill(dataSet, "Comments");

            DataRelation dr = new DataRelation("FK_comments_users", dataSet.Tables["users"].Columns["id"], dataSet.Tables["comments"].Columns["id_user"]);
            dataSet.Relations.Add(dr);
            bsUsers = new BindingSource();
            bsComments = new BindingSource();

            dataGridUsers.DataSource = bsUsers;
            dataGridComments.DataSource = bsComments;
            bsUsers.DataSource = dataSet;
            bsUsers.DataMember = "users";

            bsComments.DataSource = bsUsers;
            bsComments.DataMember = "FK_comments_users";
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void dataGridComments_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void AddComment(object sender)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                conn.Open();
                MessageBox.Show("Connection is " + conn.State);

                SqlCommandBuilder builder = new SqlCommandBuilder(adapterComments);

                adapterComments.SelectCommand.Connection = conn;
                adapterComments.Update(dataSet.Tables["comments"]);
                dataSet.Tables["comments"].Clear();
                adapterComments.Fill(dataSet, "comments");

                conn.Close();
            }
            catch (Exception error)
            {
                MessageBox.Show(error.Message);
            }
        }
    }
}
