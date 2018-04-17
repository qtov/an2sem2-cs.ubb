function to_number(str)
{
    if (!isNaN(str)) {
        return Number(str);
    }

    return str;
}

function get_sorted_index_array(arr)
{
    line_index = [];

    line_index[0] = NaN;

    for (var j = 1; j < arr.length; ++j) {
        line_index[j] = j;
    }

    var ok = false;
    while (!ok) {
        ok = true;
        for (var j = 1; j < line_index.length - 1; ++j) {
            var a = arr[line_index[j]];
            var b = arr[line_index[j + 1]];
            if (a > b) {
                if (to_number(a) > to_number(b)) {
                    var aux = line_index[j];
                    line_index[j] = line_index[j + 1];
                    line_index[j + 1] = aux;
                    ok = false;
                }
            }
        }
    }

    return line_index;
}

function construct_table(ptable)
{
    var n = ptable.rows.length;
    var m = ptable.rows[0].cells.length;
    var matrix = [];
    for (var j = 0; j < m; ++j) {
        matrix[j] = [];

        for (var i = 0; i < n; ++i) {
            matrix[j][i] = ptable.rows[i].cells[j].innerHTML;
        }
    }

    return matrix;
}

function remove_underlines(ptable)
{
    for (var i = 0; i < ptable.rows[0].cells.length; ++i)
    {
        ptable.rows[0].cells[i].style['text-decoration'] = 'none';
    }
}

var tables_full = document.getElementsByClassName("sorting_table");

for (var table_i = 0; table_i < tables_full.length; ++table_i) {
    var ptable = tables_full[table_i];
    var table_values = construct_table(ptable);

    for (var i = 0; i < ptable.rows[0].cells.length; ++i) {
        ptable.rows[0].cells[i].onclick = function(ev) {
            var rowIndex = ev.target.parentElement.rowIndex;
            var cellIndex = ev.target.cellIndex;

            line_index = get_sorted_index_array(table_values[cellIndex]);

            for (var j = 1; j < ptable.rows.length; ++j) {
                for (var k = 0; k < ptable.rows[0].cells.length; ++k) {
                    ptable.rows[j].cells[k].innerHTML = table_values[k][line_index[j]];
                }
            }

            table_values = construct_table(ptable);
            remove_underlines(ptable);
            ptable.rows[rowIndex].cells[cellIndex].style['text-decoration'] = 'underline';
        }
    }
}
