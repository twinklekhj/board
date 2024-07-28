import React from 'react';
import { useTable, useSortBy, Column } from 'react-table';

interface Board {
    id: number;
    title: string;
    writer: string;
    hits: number;
}

const BoardList: React.FC = () => {
    const data = React.useMemo<Board[]>(
        () => [
            {
                id: 1,
                title: 'Hello World',
                writer: 'khj',
                hits: 10,
            },
            {
                id: 2,
                title: 'React Table',
                writer: 'john',
                hits: 20,
            },
            {
                id: 3,
                title: 'Sorting Example',
                writer: 'doe',
                hits: 5,
            },
        ],
        []
    );

    const columns = React.useMemo<Column<Board>[]>(
        () => [
            {
                Header: 'ID',
                accessor: 'id',
            },
            {
                Header: 'Title',
                accessor: 'title',
            },
            {
                Header: 'Writer',
                accessor: 'writer',
            },
            {
                Header: 'Hits',
                accessor: 'hits',
            },
        ],
        []
    );

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({ columns, data }, useSortBy);

    return (
        <table {...getTableProps()}>
            <thead>
            {headerGroups.map(headerGroup => (
                <tr {...headerGroup.getHeaderGroupProps()} key={headerGroup.id}>
                    {headerGroup.headers.map(column => (
                        <th
                            {...column.getHeaderProps(column.getSortByToggleProps())}
                            key={column.id}
                            style={{
                                cursor: 'pointer',
                            }}
                        >
                            {column.render('Header')}
                            <span>
                  {column.isSorted
                      ? column.isSortedDesc
                          ? '🔽'
                          : '🔼'
                      : ''}
                </span>
                        </th>
                    ))}
                </tr>
            ))}
            </thead>
            <tbody {...getTableBodyProps()}>
            {rows.map(row => {
                prepareRow(row);
                return (
                    <tr {...row.getRowProps()} key={row.id}>
                        {row.cells.map(cell => (
                            <td {...cell.getCellProps()} key={cell.column.id}>
                                {cell.render('Cell')}
                            </td>
                        ))}
                    </tr>
                );
            })}
            </tbody>
        </table>
    );
};

export default BoardList;
