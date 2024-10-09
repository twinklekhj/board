import {ChangeEvent, useCallback, useEffect, useMemo, useState} from "react";
import {useSelector} from "react-redux";
import {RootState} from "@Store/index";
import {Ajax} from "@Utils/ajax";
import {useNavigate, useParams} from "react-router-dom";
import AlertUtil from "@Utils/alert";

export interface Board {
    id?: number;
    title: string;
    content?: string;
    writerId?: number;
    writer?: string;
    writerImageUrl?: string;
    visible?: boolean;
    hits?: number;
    createDate?: string;
    editDate?: string;
}

export interface PaginationParam {
    pageIndex: number;
    pageSize: number;
}

export interface BoardSearchParam {
    title?: string;
    content?: string;
    writer?: string;
    all?: string;
    sortField?: string;
    sortDir?: string;
}

export function useBoardList(pagination: PaginationParam, param: BoardSearchParam): [Board[], number, boolean, boolean] {
    const accessToken = useSelector((state: RootState) => state.token.accessToken);
    const bearerType = useSelector((state: RootState) => state.token.bearerType);
    const csrfToken = useSelector((state: RootState) => state.csrf)

    const [data, setData] = useState<Board[]>([]);
    const [pageCount, setPageCount] = useState<number>(0);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [isError, setIsError] = useState<boolean>(false);

    const fetchData = useCallback(async () => {
        setIsLoading(true);
        setIsError(false);

        try {
            const res = await Ajax({
                url: '/api/boards',
                method: 'POST',
                token: {accessToken, bearerType},
                body: {
                    pageIdx: pagination.pageIndex + 1,
                    pageSize: pagination.pageSize,
                    ...param,
                },
                csrf: csrfToken
            });
            const result = await res.json();

            setData(result.items);
            setPageCount(result.pageCnt);
        } catch (err) {
            setIsError(true);
            console.error("Failed to fetch data:", err);
        } finally {
            setIsLoading(false);
        }
    }, [accessToken, bearerType, pagination, param]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    return [data, pageCount, isLoading, isError];
}

export function useBoardDetail(boardId: string | undefined) {
    const navigate = useNavigate();

    const accessToken = useSelector((state: RootState) => state.token.accessToken);
    const bearerType = useSelector((state: RootState) => state.token.bearerType);
    const csrfToken = useSelector((state: RootState) => state.csrf)

    const memberId = useSelector((state: RootState) => state.member.id);

    const [board, setBoard] = useState<Board | null>(null);

    const onUpdateClick = () => {
        navigate(`/board/${board?.id}/update`);
    };

    const onDeleteClick = () => {
        if (!boardId) return;

        AlertUtil.confirm({
            title: '게시글 삭제',
            text: '게시글을 삭제하시겠습니까?<br>한번 삭제되면 복구가 불가능합니다.',
            onHide: result => {
                if (result.isConfirmed) {
                    Ajax({
                        url: `/api/board/${boardId}`,
                        method: 'DELETE',
                        token: {
                            accessToken: accessToken,
                            bearerType: bearerType
                        },
                        csrf: csrfToken
                    }).then(res => {
                        if (res.ok) {
                            AlertUtil.success({
                                text: '게시글이 성공적으로 삭제되었습니다! <br> 목록으로 이동합니다.',
                                onHide: result => {
                                    navigate('/');
                                }
                            })
                        }
                    }).catch(error => {
                        AlertUtil.error({
                            title: '에러 발생!',
                            text: error,
                        })
                    });
                }
            }
        })
    }

    const fetchData = useCallback(() => {
        if (!boardId) return;

        Ajax({
            url: `/api/board/${boardId}`,
            method: 'GET',
            token: {
                accessToken,
                bearerType,
            },
            csrf: csrfToken
        })
            .then(res => res.json())
            .then(res => {
                setBoard(res);
                console.log(res)
            })
            .catch(error => {
                AlertUtil.error({
                    title: '에러 발생!',
                    text: error,
                    onHide: (result) => {
                        window.history.back();
                    }
                })
            });
    }, [accessToken, bearerType, boardId]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    return {board, memberId, onUpdateClick, onDeleteClick};
}

export function useBoardAdd() {
    const navigate = useNavigate();
    const accessToken = useSelector((state: RootState) => state.token.accessToken);
    const bearerType = useSelector((state: RootState) => state.token.bearerType);
    const csrfToken = useSelector((state: RootState) => state.csrf)

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [isShow, setIsShow] = useState(true);

    const onTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
    };

    const onContentChange = (value: string | undefined) => {
        if (value !== undefined) {
            setContent(value);
        }
    };

    const onShowChange = (e: ChangeEvent<HTMLInputElement>) => {
        setIsShow(e.target.checked);
    };

    const onPublishClick = () => {
        if (title.length <= 1) {
            AlertUtil.warning({
                text: '제목을 입력해주세요!'
            });
            return false;
        }
        if (content.length <= 1) {
            AlertUtil.warning({
                text: '내용을 입력해주세요!'
            });
            return false;
        }

        Ajax({
            url: '/api/board',
            method: 'PUT',
            body: {
                title: title,
                content: content,
                visible: isShow
            },
            token: {
                accessToken: accessToken,
                bearerType: bearerType
            },
            csrf: csrfToken
        }).then((res: Response) => {
            if (res.status === 201) {
                AlertUtil.success({
                    text: '성공적으로 등록되었습니다!<br>게시글을 확인하시겠습니까?',
                    onHide: (result) => {
                        if (result.isConfirmed) {
                            const createdUri = res.headers.get('Location');
                            if (createdUri) {
                                navigate(createdUri.replace('/api', ''));
                            }
                        } else {
                            AlertUtil.info({
                                text: '메인페이지로 이동합니다.',
                                onHide: () => {
                                    navigate('/');
                                }
                            });
                        }
                    }
                });
            }
        }).catch(error => {
            AlertUtil.error({
                title: '에러 발생!',
                text: error,
            })
        });

        return true;
    };

    return {title, content, isShow, onTitleChange, onContentChange, onShowChange, onPublishClick};
}

export function useBoardUpdate() {
    const navigate = useNavigate();
    const {boardId} = useParams();

    const accessToken = useSelector((state: RootState) => state.token.accessToken);
    const bearerType = useSelector((state: RootState) => state.token.bearerType);
    const csrfToken = useSelector((state: RootState) => state.csrf)

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [isShow, setIsShow] = useState(true);

    const fetchData = useMemo(() => {
        return () => {
            Ajax({
                url: `/api/board/${boardId}`,
                method: 'GET',
                token: {
                    accessToken: accessToken,
                    bearerType: bearerType,
                },
                csrf: csrfToken
            }).then(res => res.json())
                .then(res => {
                    setTitle(res.title);
                    setContent(res.content);
                    setIsShow(res.visible);
                })
                .catch(err => {
                    AlertUtil.error({
                        text: '잘못된 요청입니다!',
                        onHide: result => {
                            setTimeout(() => {
                                window.history.back();
                            }, 1000);
                        }
                    });
                });
        };
    }, [accessToken, bearerType, boardId]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const onTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setTitle(e.target.value);
    };

    const onContentChange = (value: string | undefined) => {
        if (value !== undefined) {
            setContent(value);
        }
    };

    const onVisibleChange = (e: ChangeEvent<HTMLInputElement>) => {
        setIsShow(e.target.checked);
    };

    const goBack = () => {
        AlertUtil.confirm({
            title: '나가기 알림',
            text: '정말로 나가시겠습니까?<br>수정 내용이 초기화됩니다.',
            onHide: (result) => {
                if (result.isConfirmed) {
                    navigate('/');
                }
            }
        });
    };

    const publish = () => {
        if (title.length <= 1) {
            AlertUtil.warning({
                text: '제목을 입력해주세요!'
            });
            return false;
        }
        if (content.length <= 1) {
            AlertUtil.warning({
                text: '내용을 입력해주세요!'
            });
            return false;
        }

        Ajax({
            url: `/api/board/${boardId}`,
            method: 'PATCH',
            body: {
                title: title,
                content: content,
                visible: isShow
            },
            token: {
                accessToken: accessToken,
                bearerType: bearerType
            },
            csrf: csrfToken
        }).then((res: Response) => {
            if (res.status === 204) {
                AlertUtil.success({
                    text: '성공적으로 수정되었습니다!',
                    onHide: (result) => {
                        window.history.back();
                    }
                });
            }
        })
            .catch(error => {
                AlertUtil.error({
                    title: '에러 발생!',
                    text: error,
                })
            });
    };

    return {
        title,
        content,
        isShow,
        onTitleChange,
        onContentChange,
        onVisibleChange,
        goBack,
        publish,
    };
}
