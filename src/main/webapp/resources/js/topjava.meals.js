var mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$.ajaxSetup({
    converters: {
        "text json": function (stringData) {
            const json = JSON.parse(stringData);
            $(json).each(function () {
                this.dateTime = this.dateTime.replace('T', ' ').substr(0, 16);
            });
            return json;
        }
    }
});

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableApi: $("#datatable").DataTable({
            "ajax": {
                "url": mealAjaxUrl,
                "dataSrc": ""
            },
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "render": renderEditBtn,
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "render": renderDeleteBtn,
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        }),
        updateTable: updateFilteredTable
    });

    $.datetimepicker.setLocale(localeCode);

    const startDate = $('#startDate');
    const endDate = $('#endDate');
    startDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                maxDate: endDate.val() ? endDate.val() : false
            })
        }
    });
    endDate.datetimepicker({
        timepicker: false,
        format: 'Y-m-d',
        formatDate: 'Y-m-d',
        onShow: function (ct) {
            this.setOptions({
                minDate: startDate.val() ? startDate.val() : false
            })
        }
    });

    const startTime = $('#startTime');
    const endTime = $('#endTime');
    startTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                maxTime: endTime.val() ? endTime.val() : false
            })
        }
    });
    endTime.datetimepicker({
        datepicker: false,
        format: 'H:i',
        onShow: function (ct) {
            this.setOptions({
                minTime: startTime.val() ? startTime.val() : false
            })
        }
    });

    $('#dateTime').datetimepicker({
        format: 'Y-m-d H:i'
    });
});