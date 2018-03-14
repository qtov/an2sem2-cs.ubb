using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Data.SqlClient;

namespace ConsoleApp1
{
    class Program
    {
        static void Main(string[] args)
        {
            string connectionString = "Server=SIBYL2; Database=sem7; Integrated Security=true;";
            try {
                using (SqlConnection connection = new SqlConnection(connectionString)) {
                    Console.WriteLine(connection.State);
                    connection.Open();
                    Console.WriteLine(connection.State);
                    SqlCommand selectCommand = new SqlCommand("select Nume, CodTT from Trenuri", connection);
                    SqlCommand insertCommand = new SqlCommand("insert into Trenuri (nume, CodTT) values (@n, @ctt)", connection);

                    string name = "whatever8";
                    int ctt = 1;

                    insertCommand.Parameters.AddWithValue("@n", name);
                    insertCommand.Parameters.AddWithValue("@ctt", ctt);

                    int insertRowCount = insertCommand.ExecuteNonQuery();
                    Console.WriteLine("The number of rows affected by insert is: {0}", insertRowCount);

                    SqlDataReader reader = selectCommand.ExecuteReader();
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            Console.WriteLine("{0}\t{1}\n", reader.GetString(0), reader.GetByte(1));
                        }
                    }
                    else
                        Console.WriteLine("no con");
                    reader.Close();

                    string nm1 = "sumthing";
                    string nm2 = "whatever1";
                    SqlCommand updateCommand = new SqlCommand("update Trenuri set nume=@nm1 where nume=@nm2", connection);
                    updateCommand.Parameters.AddWithValue("@nm1", nm1);
                    updateCommand.Parameters.AddWithValue("@nm2", nm2);

                    int updateRowCount = updateCommand.ExecuteNonQuery();
                    Console.WriteLine("The number of rows affected is {0}", updateRowCount);

                    SqlCommand deleteCommand = new SqlCommand("delete from Trenuri where nume=@nume", connection);
                    deleteCommand.Parameters.AddWithValue("@nume", nm1);

                    int deleteRowCount = deleteCommand.ExecuteNonQuery();
                    Console.WriteLine("Deleted blah blah blah: {0}", deleteRowCount);

                    reader = selectCommand.ExecuteReader();
                    if (reader.HasRows)
                    {
                        while (reader.Read())
                        {
                            Console.WriteLine("{0}\t{1}\n", reader.GetString(0), reader.GetByte(1));
                        }
                    }
                    else
                        Console.WriteLine("no con");
                    reader.Close();
                }
            }
            catch (Exception e) {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine(e.Message);
            }
            Console.ReadKey();
        }
    }
}
