import React, {useEffect, useMemo, useState} from 'react';
import {Board, BoardSearchParam, useBoardList} from "@Hooks/board";
import {
    MaterialReactTable,
    type MRT_ColumnDef,
    MRT_ColumnFiltersState,
    MRT_SortingState,
    useMaterialReactTable
} from 'material-react-table';
import {useNavigate} from "react-router-dom";
import {FaLock} from "react-icons/fa6";

const BoardList: React.FC = () => {
    const navigate = useNavigate();
    const [params, setParams] = useState<BoardSearchParam>({});

    // 페이징 옵션
    const [pagination, setPagination] = useState({
        pageIndex: 0,
        pageSize: 10
    });
    // 검색 옵션
    const [columnFilters, setColumnFilters] = useState<MRT_ColumnFiltersState>([]);
    // 정렬 옵션
    const [sorting, setSorting] = useState<MRT_SortingState>([]);

    const [data, pageCount, isLoading, isError] = useBoardList(pagination, params);

    // 검색 조건 생성
    useEffect(() => {
        const updatedParams: BoardSearchParam = {
            ...Object.fromEntries(columnFilters.map(filter => [filter.id, filter.value])),
            ...(sorting.length > 0 && {
                sortField: sorting[0].id,
                sortDir: sorting[0].desc ? 'desc' : 'asc'
            })
        };

        // 조건이 변경된 경우에만 setParams 호출
        if (JSON.stringify(params) !== JSON.stringify(updatedParams)) {
            setParams(updatedParams);
        }
    }, [columnFilters, sorting]);


    const columns = useMemo<MRT_ColumnDef<Board>[]>(
        () => [
            {accessorKey: 'id', header: '글번호', size: 100, enableColumnFilter: false},
            {
                accessorKey: 'title', header: '제목', size: 500, Cell: ({renderedCellValue, row}) => (
                    <>
                        <span className={"mr-1"}>{!row.original.visible ? <FaLock/> : null}</span>
                        <span>{renderedCellValue}</span>
                    </>
                )
            },
            {accessorKey: 'writer', header: '글쓴이', size: 150},
            {accessorKey: 'createDate', header: '날짜', size: 150, enableColumnFilter: false},
            {accessorKey: 'hits', header: '조회수', size: 100, enableColumnFilter: false},
        ],
        []
    );

    const table = useMaterialReactTable({
        columns,
        data,
        manualFiltering: true,
        onColumnFiltersChange: setColumnFilters,
        manualPagination: true,
        rowCount: pageCount * pagination.pageSize,
        manualSorting: true,
        onSortingChange: setSorting,
        muiPaginationProps: {
            color: 'primary',
            shape: 'rounded',
            showRowsPerPage: false,
            variant: 'outlined',
        },
        muiToolbarAlertBannerProps: isError ? {
            color: 'error',
            children: 'Error loading data',
        } : undefined,
        paginationDisplayMode: 'pages',
        pageCount,
        state: {
            pagination,
            columnFilters,
            sorting,
            showAlertBanner: isError,
            showProgressBars: isLoading,
        },
        onPaginationChange: setPagination,
        muiTableBodyRowProps: ({row}) => ({
            onClick: (event) => {
                navigate(`/board/${row.original.id}`);
            },
            sx: {
                cursor: 'pointer', //you might want to change the cursor too when adding an onClick
            },
        }),
    });

    return <MaterialReactTable table={table}/>;
};

export default BoardList;
