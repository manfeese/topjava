// $(document).ready(function () {
const userAjaxUrl = "ajax/admin/users/";

function enable(chkbox, id) {
    const enabled = chkbox.is(":checked");
    $.ajax({
        url: userAjaxUrl + id,
        type: "POST",
        data: "enabled=" + enabled
    }).done(function () {
        chkbox.closest("tr").attr("data-userEnabled", enabled);
        successNoty(enabled ? "enabled" : "disabled");
    }).fail(function () {
        $(chkbox).prop("checked", !enabled);
    });
}

$(function () {
    makeEditable({
            ajaxUrl: userAjaxUrl,
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "name"
                    },
                    {
                        "data": "email"
                    },
                    {
                        "data": "roles"
                    },
                    {
                        "data": "enabled"
                    },
                    {
                        "data": "registered"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "asc"
                    ]
                ]
            }),
            updateTable : function () {
                $.get(userAjaxUrl, updateTableByData);
            }
        }
    );
});